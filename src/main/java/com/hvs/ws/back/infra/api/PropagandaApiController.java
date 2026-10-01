package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.propaganda.*;
import com.hvs.ws.back.app.usecase.propaganda.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/propaganda")
public class PropagandaApiController {

    private final CreatePropagandaUseCase createPropagandaUseCase;
    private final ReadAllPropagandaUseCase readAllPropagandaUseCase;
    private final PatchPropagandaUseCase patchPropagandaUseCase;
    private final DeletePropagandaUseCase deletePropagandaUseCase;
    private final PropagandaEventHub eventos;

    public PropagandaApiController(final CreatePropagandaUseCase createPropagandaUseCase,
                                   final ReadAllPropagandaUseCase readAllPropagandaUseCase,
                                   final PatchPropagandaUseCase patchPropagandaUseCase,
                                   final DeletePropagandaUseCase deletePropagandaUseCase,
                                   final PropagandaEventHub eventos) {

        this.createPropagandaUseCase = createPropagandaUseCase;
        this.readAllPropagandaUseCase = readAllPropagandaUseCase;
        this.patchPropagandaUseCase = patchPropagandaUseCase;
        this.deletePropagandaUseCase = deletePropagandaUseCase;
        this.eventos = eventos;
    }

    @PostMapping
    public ResponseEntity<?> createPropaganda(@RequestBody CreatePropagandaCommand aInput) {

        return this.createPropagandaUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> {
                            this.eventos.publicar();
                            return new ResponseEntity<>(success, HttpStatus.OK);
                        });
    }

    @GetMapping
    public ResponseEntity<?> readAllPropaganda(@RequestParam(required = false) Long blocoId,
                                               @RequestParam(required = false) String posicao,
                                               @RequestParam(required = false) Integer pagina) {

        return this.readAllPropagandaUseCase.execute(ReadAllPropagandaCommand.from(blocoId, posicao, pagina))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    /** Pub/sub: o player se inscreve e é avisado quando a lista mudar. */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter eventos() {

        return this.eventos.inscrever();
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchPropagandaById(@PathVariable("id") Long aId,
                                                 @RequestBody PatchPropagandaCommand aInput) {

        return this.patchPropagandaUseCase.execute(PatchPropagandaCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> {
                            this.eventos.publicar();
                            return new ResponseEntity<>(success, HttpStatus.OK);
                        });
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deletePropagandaById(@PathVariable("id") Long aId) {

        return this.deletePropagandaUseCase.execute(DeletePropagandaCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> {
                            this.eventos.publicar();
                            return new ResponseEntity<>(success, HttpStatus.OK);
                        });
    }
}
