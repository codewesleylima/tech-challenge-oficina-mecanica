package com.safiap.techchallengeoficinamecanica.modules.register.presentation.controllers;


import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.PartialAlterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.RegisterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.AlterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.PartialAlterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.RegisterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.AlterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer.*;
import com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.customer.PartialAlterCustomerDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.customer.RegisterCustomerDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.customer.AlterCustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final AlterCustomerUseCase alterCustomerUseCase;
    private final PartialAlterCustomerUseCase partialAlterCustomerUseCase;
    private final GetAllCustomersUseCase getAllCustomersUseCase;
    private final GetCustomerByIdUseCase  getCustomerByIdUseCase;
    private final GetCustomerByCpfUseCase getCustomerByCpfUseCase;

    public CustomerController(RegisterCustomerUseCase registerCustomerUseCase,
                              AlterCustomerUseCase alterCustomerUseCase,
                              PartialAlterCustomerUseCase partialAlterCustomerUseCase,
                              GetAllCustomersUseCase getAllCustomersUseCase,
                              GetCustomerByIdUseCase getCustomerByIdUseCase,
                              GetCustomerByCpfUseCase getCustomerByCpfUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.alterCustomerUseCase = alterCustomerUseCase;
        this.partialAlterCustomerUseCase = partialAlterCustomerUseCase;
        this.getAllCustomersUseCase = getAllCustomersUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
        this.getCustomerByCpfUseCase = getCustomerByCpfUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(
            @RequestBody RegisterCustomerDTO request) {

        RegisterCustomerCommand command = new RegisterCustomerCommand(
                request.Name(),
                request.Email(),
                request.Phone(),
                request.CPF()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(registerCustomerUseCase.execute(command));

    };

    @PutMapping("/{id}/update")
    public ResponseEntity<AlterCustomerResponse> registerCustomer(
            @PathVariable UUID id,
            @RequestBody AlterCustomerDTO request) {

        AlterCustomerCommand command = new AlterCustomerCommand(
                id,
                request.Name(),
                request.Email(),
                request.Phone(),
                request.CPF()
        );

        return ResponseEntity.status(HttpStatus.OK).body(alterCustomerUseCase.execute(command));

    };

    @PatchMapping("/{id}/update")
    public ResponseEntity<PartialAlterCustomerResponse> registerCustomer(
            @PathVariable UUID id,
            @RequestBody PartialAlterCustomerDTO request) {

        PartialAlterCustomerCommand command = new PartialAlterCustomerCommand(
                id,
                request.Name(),
                request.Email(),
                request.Phone(),
                request.CPF()
        );

        return ResponseEntity.status(HttpStatus.OK).body(partialAlterCustomerUseCase.execute(command));

    };

    @GetMapping()
    public ResponseEntity<List<GetCustomerResponse>> findAllCustomer() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllCustomersUseCase.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerResponse> findCustomerById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(getCustomerByIdUseCase.execute(id));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<GetCustomerResponse> findCustomerByCPF(
            @PathVariable String cpf
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(getCustomerByCpfUseCase.execute(cpf));
    }


}
