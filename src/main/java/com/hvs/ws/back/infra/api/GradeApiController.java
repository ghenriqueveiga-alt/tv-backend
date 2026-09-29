package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.grade.*;
import com.hvs.ws.back.app.usecase.grade.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grade")
public class GradeApiController {

    private final CreateGradeUseCase createGradeUseCase;
    private final ReadGradeUseCase readGradeUseCase;
    private final ReadAllGradeUseCase readAllGradeUseCase;
    private final UpdateGradeUseCase updateGradeUseCase;
    private final PatchGradeUseCase patchGradeUseCase;
    private final DeleteGradeUseCase deleteGradeUseCase;

    public GradeApiController(final CreateGradeUseCase createGradeUseCase,
                              final ReadGradeUseCase readGradeUseCase,
                              final ReadAllGradeUseCase readAllGradeUseCase,
                              final UpdateGradeUseCase updateGradeUseCase,
                              final PatchGradeUseCase patchGradeUseCase,
                              final DeleteGradeUseCase deleteGradeUseCase) {

        this.createGradeUseCase = createGradeUseCase;
        this.readGradeUseCase = readGradeUseCase;
        this.readAllGradeUseCase = readAllGradeUseCase;
        this.updateGradeUseCase = updateGradeUseCase;
        this.patchGradeUseCase = patchGradeUseCase;
        this.deleteGradeUseCase = deleteGradeUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createGrade(@RequestBody CreateGradeCommand aInput) {

        return this.createGradeUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<?> readGradeById(@PathVariable("id") Long aId) {

        return this.readGradeUseCase.execute(ReadGradeCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> readGradeByUuid(@PathVariable("uuid") String aUuid) {

        return this.readGradeUseCase.execute(ReadGradeCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<?> readAllGrade(@RequestParam(required = false) String search,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sort,
                                          @RequestParam(defaultValue = "asc") String direction) {

        return this.readAllGradeUseCase.execute(new ReadAllGradeCommand(
                        new GradeSearchQuery(search, page, size, sort, direction)))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateGradeById(@PathVariable("id") Long aId,
                                             @RequestBody UpdateGradeCommand aInput) {

        return this.updateGradeUseCase.execute(UpdateGradeCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> updateGradeByUuid(@PathVariable("uuid") String aUuid,
                                               @RequestBody UpdateGradeCommand aInput) {

        return this.updateGradeUseCase.execute(UpdateGradeCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchGradeById(@PathVariable("id") Long aId,
                                            @RequestBody PatchGradeCommand aInput) {

        return this.patchGradeUseCase.execute(PatchGradeCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> patchGradeByUuid(@PathVariable("uuid") String aUuid,
                                              @RequestBody PatchGradeCommand aInput) {

        return this.patchGradeUseCase.execute(PatchGradeCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deleteGradeById(@PathVariable("id") Long aId) {

        return this.deleteGradeUseCase.execute(DeleteGradeCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> deleteCorteByUuid(@PathVariable("uuid") String aUuid) {

        return this.deleteGradeUseCase.execute(DeleteGradeCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }
}
