package com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.AlterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.RegisterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.RegisterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.AlterPartUseCase;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.DeletePartUseCase;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.RegisterPartUseCase;
import com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.DTO.part.AlterPartDTO;
import com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.DTO.part.RegisterPartDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/part")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class PartController {

    private final RegisterPartUseCase registerPartUseCase;
    private final AlterPartUseCase alterPartUseCase;
    private final DeletePartUseCase deletePartUseCase;

    public PartController(RegisterPartUseCase registerPartUseCase,
                          AlterPartUseCase alterPartUseCase,
                          DeletePartUseCase deletePartUseCase) {
        this.registerPartUseCase = registerPartUseCase;
        this.alterPartUseCase = alterPartUseCase;
        this.deletePartUseCase = deletePartUseCase;
    }

    @PostMapping
    public ResponseEntity<RegisterPartResponse> registerPart(
            @RequestBody RegisterPartDTO request) {

        RegisterPartCommand command = new RegisterPartCommand(
                request.name(),
                request.description(),
                request.quantity(),
                request.price()
        );

        RegisterPartResponse response = registerPartUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlterPartResponse> alterPart(
            @PathVariable UUID id,
            @RequestBody AlterPartDTO request) {

        AlterPartCommand command = new AlterPartCommand(
                request.name(),
                request.description(),
                request.price()
        );

        return ResponseEntity.ok(alterPartUseCase.execute(command, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable UUID id) {
        deletePartUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
