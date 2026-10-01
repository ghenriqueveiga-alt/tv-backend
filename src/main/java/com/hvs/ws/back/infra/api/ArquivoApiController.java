package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.arquivo.*;
import com.hvs.ws.back.app.usecase.arquivo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/arquivo")
public class ArquivoApiController {

    /** Extensões aceitas como vídeo de propaganda (a mesma lista do front). */
    private static final Set<String> VIDEO_EXTS =
            Set.of(".mp4", ".mkv", ".webm", ".mov", ".avi", ".flv",
                   ".mpg", ".mpeg", ".m4v", ".wmv", ".rmvb");

    private final CreateArquivoUseCase createArquivoUseCase;
    private final ReadArquivoUseCase readArquivoUseCase;
    private final ReadAllArquivoUseCase readAllArquivoUseCase;
    private final UpdateArquivoUseCase updateArquivoUseCase;
    private final PatchArquivoUseCase patchArquivoUseCase;
    private final DeleteArquivoUseCase deleteArquivoUseCase;

    /** Raiz das mídias (`webstore.media.root`); uploads caem em `propagandas/`. */
    private final String mediaRoot;

    public ArquivoApiController(final CreateArquivoUseCase createArquivoUseCase,
                                final ReadArquivoUseCase readArquivoUseCase,
                                final ReadAllArquivoUseCase readAllArquivoUseCase,
                                final UpdateArquivoUseCase updateArquivoUseCase,
                                final PatchArquivoUseCase patchArquivoUseCase,
                                final DeleteArquivoUseCase deleteArquivoUseCase,
                                @Value("${webstore.media.root:./media}") final String mediaRoot) {

        this.createArquivoUseCase = createArquivoUseCase;
        this.readArquivoUseCase = readArquivoUseCase;
        this.readAllArquivoUseCase = readAllArquivoUseCase;
        this.updateArquivoUseCase = updateArquivoUseCase;
        this.patchArquivoUseCase = patchArquivoUseCase;
        this.deleteArquivoUseCase = deleteArquivoUseCase;
        this.mediaRoot = mediaRoot;
    }

    @PostMapping
    public ResponseEntity<?> createArquivo(@RequestBody CreateArquivoCommand aInput) {

        return this.createArquivoUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    /**
     * Envia o vídeo da propaganda: grava o arquivo em `{mediaRoot}/propagandas/`
     * e cria o registro do catálogo (nome original, extensão, tamanho, caminho).
     * Devolve o arquivo criado já com o id, para a propaganda associar.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadArquivo(@RequestParam("file") final MultipartFile aFile) {

        final String original = aFile.getOriginalFilename() != null ? aFile.getOriginalFilename().trim() : "";
        final String ext = extrairExtensao(original);

        if (aFile.isEmpty() || original.isEmpty()) {
            return new ResponseEntity<>("Arquivo vazio.", HttpStatus.BAD_REQUEST);
        }
        if (!VIDEO_EXTS.contains(ext)) {
            return new ResponseEntity<>("Formato de video nao suportado: " + ext, HttpStatus.BAD_REQUEST);
        }

        final Path pasta = Paths.get(this.mediaRoot, "propagandas");
        final Path destino = pasta.resolve(UUID.randomUUID() + ext).toAbsolutePath();

        try {
            Files.createDirectories(pasta);
            aFile.transferTo(destino.toFile());
        } catch (IOException e) {
            return new ResponseEntity<>("Falha ao gravar o arquivo: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final String nome = original.length() > 200 ? original.substring(0, 200) : original;
        final String caminho = destino.toString().replace('\\', '/');

        return this.createArquivoUseCase
                .execute(new CreateArquivoCommand(nome, ext, aFile.getSize(), caminho))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> this.readArquivoUseCase.execute(ReadArquivoCommand.from(success.aId()))
                                .fold(readError -> new ResponseEntity<>(success, HttpStatus.OK),
                                        readOk -> new ResponseEntity<>(readOk, HttpStatus.OK)));
    }

    /** "meu_video.MP4" → ".mp4" (o catálogo guarda a extensão com o ponto). */
    private static String extrairExtensao(final String aNome) {
        final int ponto = aNome.lastIndexOf('.');
        return ponto < 0 ? "" : aNome.substring(ponto).toLowerCase();
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<?> readArquivoById(@PathVariable("id") Long aId) {

        return this.readArquivoUseCase.execute(ReadArquivoCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> readArquivoByUuid(@PathVariable("uuid") String aUuid) {

        return this.readArquivoUseCase.execute(ReadArquivoCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<?> readAllArquivo(@RequestParam(required = false) String search,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sort,
                                            @RequestParam(defaultValue = "asc") String direction) {

        return this.readAllArquivoUseCase.execute(new ReadAllArquivoCommand(
                        new ArquivoSearchQuery(search, page, size, sort, direction)))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateArticleById(@PathVariable("id") Long aId,
                                               @RequestBody UpdateArquivoCommand aInput) {

        return this.updateArquivoUseCase.execute(UpdateArquivoCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> updateArticleByUuid(@PathVariable("uuid") String aUuid,
                                                 @RequestBody UpdateArquivoCommand aInput) {

        return this.updateArquivoUseCase.execute(UpdateArquivoCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchArquivoById(@PathVariable("id") Long aId,
                                              @RequestBody PatchArquivoCommand aInput) {

        return this.patchArquivoUseCase.execute(PatchArquivoCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> patchArquivoByUuid(@PathVariable("uuid") String aUuid,
                                                @RequestBody PatchArquivoCommand aInput) {

        return this.patchArquivoUseCase.execute(PatchArquivoCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deleteArquivoById(@PathVariable("id") Long aId) {

        return this.deleteArquivoUseCase.execute(DeleteArquivoCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> deleteArquivoByUuid(@PathVariable("uuid") String aUuid) {

        return this.deleteArquivoUseCase.execute(DeleteArquivoCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/{id}/stream")
    public ResponseEntity<StreamingResponseBody> streamArquivo(@PathVariable("id") Long aId,
                                                               @RequestHeader(value = "Range", required = false) String aRange) {

        return this.readArquivoUseCase.execute(ReadArquivoCommand.from(aId))
                .fold(error -> ResponseEntity.notFound().<StreamingResponseBody>build(),
                        output -> {
                            final String resolved = output.aCaminho();
                            final File file = new File(resolved);

                            if (!file.exists()) {
                                return ResponseEntity.notFound().<StreamingResponseBody>build();
                            }

                            final String contentType = resolveContentType(output.aTipo());
                            final long fileLength = file.length();

                            if (aRange == null) {
                                final StreamingResponseBody stream = outputStream -> {
                                    try (var in = new BufferedInputStream(new FileInputStream(file))) {
                                        in.transferTo(outputStream);
                                    }
                                };
                                return ResponseEntity.ok()
                                        .contentType(MediaType.parseMediaType(contentType))
                                        .contentLength(fileLength)
                                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                                        .body(stream);
                            }

                            final long[] range = parseRange(aRange, fileLength);
                            final long start = range[0];
                            final long end = range[1];
                            final long contentLength = end - start + 1;

                            final StreamingResponseBody stream = outputStream -> {
                                try (var in = new BufferedInputStream(new FileInputStream(file))) {
                                    in.skip(start);
                                    final byte[] buffer = new byte[8192];
                                    long remaining = contentLength;
                                    int read;
                                    while (remaining > 0 && (read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                                        outputStream.write(buffer, 0, read);
                                        remaining -= read;
                                    }
                                }
                            };

                            final HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.parseMediaType(contentType));
                            headers.set(HttpHeaders.CONTENT_RANGE,
                                    "bytes " + start + "-" + end + "/" + fileLength);
                            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
                            headers.setContentLength(contentLength);

                            return new ResponseEntity<>(stream, headers, HttpStatus.PARTIAL_CONTENT);
                        });
    }

    private long[] parseRange(final String aRange, final long aFileLength) {
        final String rangeValue = aRange.replace("bytes=", "");
        final String[] parts = rangeValue.split("-");

        long start = Long.parseLong(parts[0]);
        long end = parts.length > 1 && !parts[1].isEmpty()
                ? Long.parseLong(parts[1])
                : aFileLength - 1;

        if (end >= aFileLength) {
            end = aFileLength - 1;
        }

        return new long[]{start, end};
    }

    private static String resolveContentType(final String aTipo) {
        if (aTipo == null || aTipo.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        // o catálogo guarda a extensão com ponto (".mp4"); sem tirar, viraria "video/.mp4"
        final String lower = aTipo.toLowerCase().trim();
        final String ext = lower.startsWith(".") ? lower.substring(1) : lower;
        return switch (ext) {
            case "mp4", "m4v" -> "video/mp4";
            case "avi" -> "video/x-msvideo";
            case "mkv" -> "video/x-matroska";
            case "flv" -> "video/x-flv";
            case "rmvb" -> "application/vnd.rn-realmedia-vbr";
            case "mov" -> "video/quicktime";
            case "wmv" -> "video/x-ms-wmv";
            case "webm" -> "video/webm";
            case "mpg", "mpeg" -> "video/mpeg";
            default -> ext.contains("/") ? ext : "video/" + ext;
        };
    }
}
