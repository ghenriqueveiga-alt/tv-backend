package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.propaganda.*;
import com.hvs.ws.back.app.usecase.propaganda.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/propaganda")
public class PropagandaApiController {

    private final CreatePropagandaUseCase createPropagandaUseCase;
    private final ReadAllPropagandaUseCase readAllPropagandaUseCase;
    private final PatchPropagandaUseCase patchPropagandaUseCase;
    private final DeletePropagandaUseCase deletePropagandaUseCase;

    public PropagandaApiController(final CreatePropagandaUseCase createPropagandaUseCase,
                                   final ReadAllPropagandaUseCase readAllPropagandaUseCase,
                                   final PatchPropagandaUseCase patchPropagandaUseCase,
                                   final DeletePropagandaUseCase deletePropagandaUseCase) {

        this.createPropagandaUseCase = createPropagandaUseCase;
        this.readAllPropagandaUseCase = readAllPropagandaUseCase;
        this.patchPropagandaUseCase = patchPropagandaUseCase;
        this.deletePropagandaUseCase = deletePropagandaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createPropaganda(@RequestBody CreatePropagandaCommand aInput) {

        return this.createPropagandaUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<?> readAllPropaganda(@RequestParam(required = false) Long blocoId,
                                               @RequestParam(required = false) String posicao) {

        return this.readAllPropagandaUseCase.execute(ReadAllPropagandaCommand.from(blocoId, posicao))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchPropagandaById(@PathVariable("id") Long aId,
                                                 @RequestBody PatchPropagandaCommand aInput) {

        return this.patchPropagandaUseCase.execute(PatchPropagandaCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deletePropagandaById(@PathVariable("id") Long aId) {

        return this.deletePropagandaUseCase.execute(DeletePropagandaCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }
}
