package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.CustomerRegistration;
import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.RegisterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.RegisterCustomerUseCase;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerRegistrationAdapter implements CustomerRegistration {

    private final RegisterCustomerUseCase registerCustomerUseCase;

    public CustomerRegistrationAdapter(RegisterCustomerUseCase registerCustomerUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
    }

    @Override
    public UUID register(String name, String email, String phone, String cpf) {
        return registerCustomerUseCase
                .execute(new RegisterCustomerCommand(name, email, phone, cpf))
                .customerId();
    }
}
