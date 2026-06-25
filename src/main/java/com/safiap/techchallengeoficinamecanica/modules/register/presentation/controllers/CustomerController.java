package com.safiap.techchallengeoficinamecanica.modules.register.presentation.controllers;


import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.RegisterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.RegisterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.RegisterCustomerUseCase;
import com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.customer.RegisterCustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerCustomerUseCase;

    public CustomerController(RegisterCustomerUseCase registerCustomerUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(
            @RequestBody RegisterCustomerDTO request) {

        RegisterCustomerCommand command = new RegisterCustomerCommand(
                request.name(),
                request.email(),
                request.phone(),
                request.cpf()
        );

        RegisterCustomerResponse response = registerCustomerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    };
}
