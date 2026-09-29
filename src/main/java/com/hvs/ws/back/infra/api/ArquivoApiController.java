package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.arquivo.*;
import com.hvs.ws.back.app.usecase.arquivo.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@RestController
@RequestMapping("/api/v1/arquivo")
public class ArquivoApiController {

    private final CreateArquivoUseCase createArquivoUseCase;
    private final ReadArquivoUseCase readArquivoUseCase;
    private final ReadAllArquivoUseCase readAllArquivoUseCase;
    private final UpdateArquivoUseCase updateArquivoUseCase;
    private final PatchArquivoUseCase patchArquivoUseCase;
    private final DeleteArquivoUseCase deleteArquivoUseCase;

    public ArquivoApiController(final CreateArquivoUseCase createArquivoUseCase,
                                final ReadArquivoUseCase readArquivoUseCase,
                                final ReadAllArquivoUseCase readAllArquivoUseCase,
                                final UpdateArquivoUseCase updateArquivoUseCase,
                                final PatchArquivoUseCase patchArquivoUseCase,
                                final DeleteArquivoUseCase deleteArquivoUseCase) {

        this.createArquivoUseCase = createArquivoUseCase;
        this.readArquivoUseCase = readArquivoUseCase;
        this.readAllArquivoUseCase = readAllArquivoUseCase;
        this.updateArquivoUseCase = updateArquivoUseCase;
        this.patchArquivoUseCase = patchArquivoUseCase;
        this.deleteArquivoUseCase = deleteArquivoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createArquivo(@RequestBody CreateArquivoCommand aInput) {

        return this.createArquivoUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
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
        final String lower = aTipo.toLowerCase().trim();
        return switch (lower) {
            case "mp4" -> "video/mp4";
            case "avi" -> "video/x-msvideo";
            case "mkv" -> "video/x-matroska";
            case "flv" -> "video/x-flv";
            case "rmvb" -> "application/vnd.rn-realmedia-vbr";
            case "mov" -> "video/quicktime";
            case "wmv" -> "video/x-ms-wmv";
            case "webm" -> "video/webm";
            default -> lower.contains("/") ? lower : "video/" + lower;
        };
    }
}
