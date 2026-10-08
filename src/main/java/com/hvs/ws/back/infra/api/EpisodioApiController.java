package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.episodio.*;
import com.hvs.ws.back.app.usecase.episodio.*;
import com.hvs.ws.back.app.output.episodio.ReadEpisodioOutput;
import com.hvs.ws.back.infra.persistence.episodio.EpisodioJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/episodio")
@Tag(name = "Episódios", description = "Operações de CRUD, cortes de tempo e detecção de comerciais dos episódios")
public class EpisodioApiController {

private final CreateEpisodioUseCase createEpisodioUseCase;
    private final ReadEpisodioUseCase readEpisodioUseCase;
    private final ReadAllEpisodioUseCase readAllEpisodioUseCase;
    private final UpdateEpisodioUseCase updateEpisodioUseCase;
    private final PatchEpisodioUseCase patchEpisodioUseCase;
    private final DeleteEpisodioUseCase deleteEpisodioUseCase;
    private final EpisodioJpaRepository episodioJpaRepository;

    public EpisodioApiController(final CreateEpisodioUseCase createEpisodioUseCase,
                                 final ReadEpisodioUseCase readEpisodioUseCase,
                                 final ReadAllEpisodioUseCase readAllEpisodioUseCase,
                                 final UpdateEpisodioUseCase updateEpisodioUseCase,
                                 final PatchEpisodioUseCase patchEpisodioUseCase,
                                 final DeleteEpisodioUseCase deleteEpisodioUseCase,
                                 final EpisodioJpaRepository episodioJpaRepository) {

        this.createEpisodioUseCase = createEpisodioUseCase;
        this.readEpisodioUseCase = readEpisodioUseCase;
        this.readAllEpisodioUseCase = readAllEpisodioUseCase;
        this.updateEpisodioUseCase = updateEpisodioUseCase;
        this.patchEpisodioUseCase = patchEpisodioUseCase;
        this.deleteEpisodioUseCase = deleteEpisodioUseCase;
        this.episodioJpaRepository = episodioJpaRepository;
    }

    @PostMapping
    public ResponseEntity<?> createEpisodio(@RequestBody CreateEpisodioCommand aInput) {

        return this.createEpisodioUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<?> readAllEpisodio(@RequestParam(required = false) String search,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sort,
                                             @RequestParam(defaultValue = "asc") String direction) {

        return this.readAllEpisodioUseCase.execute(new ReadAllEpisodioCommand(
                        EpisodioSearchQuery.from(search, null, null, page, size, sort, direction)))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/id/{id}")
    @Operation(summary = "Busca episódio por id",
            responses = @ApiResponse(responseCode = "200", description = "Episódio encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReadEpisodioOutput.class))))
    public ResponseEntity<?> readEpisodioById(@PathVariable("id") Long aId) {

        return this.readEpisodioUseCase.execute(ReadEpisodioCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/uuid/{uuid}")
    @Operation(summary = "Busca episódio por uuid",
            responses = @ApiResponse(responseCode = "200", description = "Episódio encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReadEpisodioOutput.class))))
    public ResponseEntity<?> readEpisodioByUuid(@PathVariable("uuid") String aUuid) {

        return this.readEpisodioUseCase.execute(ReadEpisodioCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/primeiro-por-programa")
    public ResponseEntity<?> readFirstEpisodioByProgramaIds(@RequestParam String programaIds) {

        final java.util.List<Long> ids = java.util.Arrays.stream(programaIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();

        final var entities = this.episodioJpaRepository.findFirstByProgramaIds(ids);
        final var result = entities.stream()
                .map(e -> {
                    final var d = e.toDomainChildren();
                    return ReadEpisodioOutput.from(d);
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/primeiros-por-programa")
    public ResponseEntity<?> readPrimeirosEpisodiosByProgramaIds(@RequestParam String programaIds,
                                                                 @RequestParam(defaultValue = "0") int offset,
                                                                 @RequestParam(defaultValue = "1") int limite) {

        final java.util.List<Long> ids = java.util.Arrays.stream(programaIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();

        final var rows = this.episodioJpaRepository.findPrimeirosPorProgramaIds(ids, offset, limite);
        final var result = rows.stream()
                .map(row -> {
                    java.util.Map<String, Object> m = new java.util.HashMap<>();
                    m.put("aId", ((Number) row[0]).longValue());
                    m.put("aNumero", row[1] != null ? ((Number) row[1]).longValue() : null);
                    m.put("aTitulo", row[2] != null ? row[2].toString() : null);
                    m.put("aProgramaId", ((Number) row[3]).longValue());
                    m.put("aTemporada", row[4] != null ? ((Number) row[4]).longValue() : null);
                    m.put("aParte", row[5] != null ? ((Number) row[5]).longValue() : 0L);
                    m.put("aDuracao", row[6] != null ? row[6].toString() : null);
                    m.put("aCapaUrl", row[7] != null ? row[7].toString() : null);
                    m.put("aArquivoId", row[8] != null ? ((Number) row[8]).longValue() : null);
                    return m;
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}/capa")
    @Operation(summary = "Busca a imagem de capa do episódio")
    public ResponseEntity<byte[]> readCapaEpisodioById(@PathVariable("id") Long aId) {

        final var opt = this.episodioJpaRepository.findById(aId);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // capa_url guarda o caminho local da imagem no servidor,
        // por exemplo "F:/Programa/01_Titulo_imagem.jpg".
        final String capaUrl = opt.get().getCapaUrl();
        if (capaUrl == null || capaUrl.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        final File arquivo = new File(capaUrl);
        if (!arquivo.isFile() || arquivo.length() == 0) {
            return ResponseEntity.notFound().build();
        }

        try {
            final byte[] bytes = Files.readAllBytes(arquivo.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentTypeDaCapa(capaUrl)))
                    .contentLength(bytes.length)
                    .body(bytes);
        } catch (final IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String contentTypeDaCapa(final String aCaminho) {
        final String caminho = aCaminho.toLowerCase(Locale.ROOT);
        if (caminho.endsWith(".png")) return "image/png";
        if (caminho.endsWith(".webp")) return "image/webp";
        if (caminho.endsWith(".gif")) return "image/gif";
        return "image/jpeg";
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateEpisodioById(@PathVariable("id") Long aId,
                                                @RequestBody UpdateEpisodioCommand aInput) {

        return this.updateEpisodioUseCase.execute(UpdateEpisodioCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> updateEpisodioByUuid(@PathVariable("uuid") String aUuid,
                                                  @RequestBody UpdateEpisodioCommand aInput) {

        return this.updateEpisodioUseCase.execute(UpdateEpisodioCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchEpisodioById(@PathVariable("id") Long aId,
                                               @RequestBody PatchEpisodioCommand aInput) {

        return this.patchEpisodioUseCase.execute(PatchEpisodioCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> patchEpisodioByUuid(@PathVariable("uuid") String aUuid,
                                                 @RequestBody PatchEpisodioCommand aInput) {

        return this.patchEpisodioUseCase.execute(PatchEpisodioCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deleteEpisodioById(@PathVariable("id") Long aId) {

        return this.deleteEpisodioUseCase.execute(DeleteEpisodioCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> deleteCorteByUuid(@PathVariable("uuid") String aUuid) {

        return this.deleteEpisodioUseCase.execute(DeleteEpisodioCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }
}
