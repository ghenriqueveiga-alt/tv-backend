package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.anuncio.*;
import com.hvs.ws.back.app.usecase.anuncio.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/anuncio")
public class AnuncioApiController {

    private final CreateAnuncioUseCase createAnuncioUseCase;
    private final ReadAllAnuncioUseCase readAllAnuncioUseCase;
    private final PatchAnuncioUseCase patchAnuncioUseCase;
    private final DeleteAnuncioUseCase deleteAnuncioUseCase;
    private final VerificarAnuncioUseCase verificarAnuncioUseCase;

    public AnuncioApiController(final CreateAnuncioUseCase createAnuncioUseCase,
                                final ReadAllAnuncioUseCase readAllAnuncioUseCase,
                                final PatchAnuncioUseCase patchAnuncioUseCase,
                                final DeleteAnuncioUseCase deleteAnuncioUseCase,
                                final VerificarAnuncioUseCase verificarAnuncioUseCase) {

        this.createAnuncioUseCase = createAnuncioUseCase;
        this.readAllAnuncioUseCase = readAllAnuncioUseCase;
        this.patchAnuncioUseCase = patchAnuncioUseCase;
        this.deleteAnuncioUseCase = deleteAnuncioUseCase;
        this.verificarAnuncioUseCase = verificarAnuncioUseCase;
    }

    /** Com posição: o site pede (só ativos). Sem posição: a moderação lista tudo. */
    @GetMapping
    public ResponseEntity<?> readAllAnuncio(@RequestParam(required = false) Integer posicao,
                                            @RequestParam(required = false) String status) {

        return this.readAllAnuncioUseCase.execute(ReadAllAnuncioCommand.from(posicao, status))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success.anuncios(), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<?> createAnuncio(@RequestBody CreateAnuncioCommand aInput) {

        return this.createAnuncioUseCase.execute(CreateAnuncioCommand.from(aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchAnuncioById(@PathVariable("id") Long aId,
                                              @RequestBody PatchAnuncioCommand aInput) {

        return this.patchAnuncioUseCase.execute(PatchAnuncioCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    /** Consulta a blockchain; pagamento confirmado promove PENDENTE → PAGO. */
    @PostMapping(value = "/id/{id}/verificar")
    public ResponseEntity<?> verificarAnuncioById(@PathVariable("id") Long aId) {

        return this.verificarAnuncioUseCase.execute(VerificarAnuncioCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deleteAnuncioById(@PathVariable("id") Long aId) {

        return this.deleteAnuncioUseCase.execute(DeleteAnuncioCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }
}
