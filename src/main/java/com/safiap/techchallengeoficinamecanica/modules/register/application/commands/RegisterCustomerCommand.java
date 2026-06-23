package com.safiap.techchallengeoficinamecanica.modules.register.application.commands;


public record RegisterCustomerCommand(
        String name,
        String email,
        String phone,
        String cpf
) {
}
