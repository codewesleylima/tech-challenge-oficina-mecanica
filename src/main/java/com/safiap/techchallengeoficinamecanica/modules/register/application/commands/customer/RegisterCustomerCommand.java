package com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer;


public record RegisterCustomerCommand(
        String name,
        String email,
        String phone,
        String cnpjCpf
) {
}
