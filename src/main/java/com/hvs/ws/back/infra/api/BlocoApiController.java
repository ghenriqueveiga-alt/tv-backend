package com.hvs.ws.back.infra.api;

import com.hvs.ws.back.app.command.bloco.*;
import com.hvs.ws.back.app.usecase.bloco.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bloco")
public class BlocoApiController {

    private final CreateBlocoUseCase createBlocoUseCase;
    private final ReadBlocoUseCase readBlocoUseCase;
    private final ReadAllBlocoUseCase readAllBlocoUseCase;
    private final UpdateBlocoUseCase updateBlocoUseCase;
    private final PatchBlocoUseCase patchBlocoUseCase;
    private final DeleteBlocoUseCase deleteBlocoUseCase;

    public BlocoApiController(final CreateBlocoUseCase createBlocoUseCase,
                              final ReadBlocoUseCase readBlocoUseCase,
                              final ReadAllBlocoUseCase readAllBlocoUseCase,
                              final UpdateBlocoUseCase updateBlocoUseCase,
                              final PatchBlocoUseCase patchBlocoUseCase,
                              final DeleteBlocoUseCase deleteBlocoUseCase) {

        this.createBlocoUseCase = createBlocoUseCase;
        this.readBlocoUseCase = readBlocoUseCase;
        this.readAllBlocoUseCase = readAllBlocoUseCase;
        this.updateBlocoUseCase = updateBlocoUseCase;
        this.patchBlocoUseCase = patchBlocoUseCase;
        this.deleteBlocoUseCase = deleteBlocoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createBloco(@RequestBody CreateBlocoCommand aInput) {

        return this.createBlocoUseCase.execute(aInput)
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<?> readBlocoById(@PathVariable("id") Long aId) {

        return this.readBlocoUseCase.execute(ReadBlocoCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> readBlocoByUuid(@PathVariable("uuid") String aUuid) {

        return this.readBlocoUseCase.execute(ReadBlocoCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<?> readAllBloco(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) Long gradeId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sort,
                                          @RequestParam(defaultValue = "asc") String direction) {

        return this.readAllBlocoUseCase.execute(new ReadAllBlocoCommand(
                        new BlocoSearchQuery(search, gradeId, page, size, sort, direction)))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateBlocoById(@PathVariable("id") Long aId,
                                             @RequestBody UpdateBlocoCommand aInput) {

        return this.updateBlocoUseCase.execute(UpdateBlocoCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PutMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> updateBlocoByUuid(@PathVariable("uuid") String aUuid,
                                               @RequestBody UpdateBlocoCommand aInput) {

        return this.updateBlocoUseCase.execute(UpdateBlocoCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/id/{id}")
    public ResponseEntity<?> patchBlocoById(@PathVariable("id") Long aId,
                                            @RequestBody PatchBlocoCommand aInput) {

        return this.patchBlocoUseCase.execute(PatchBlocoCommand.from(aId, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @PatchMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> patchBlocoByUuid(@PathVariable("uuid") String aUuid,
                                              @RequestBody PatchBlocoCommand aInput) {

        return this.patchBlocoUseCase.execute(PatchBlocoCommand.from(aUuid, aInput))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> deleteBlocoById(@PathVariable("id") Long aId) {

        return this.deleteBlocoUseCase.execute(DeleteBlocoCommand.from(aId))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }

    @DeleteMapping(value = "/uuid/{uuid}")
    public ResponseEntity<?> deleteBlocoByUuid(@PathVariable("uuid") String aUuid) {

        return this.deleteBlocoUseCase.execute(DeleteBlocoCommand.from(aUuid))
                .fold(error -> new ResponseEntity<>(error, HttpStatus.CONFLICT),
                        success -> new ResponseEntity<>(success, HttpStatus.OK));
    }
}
