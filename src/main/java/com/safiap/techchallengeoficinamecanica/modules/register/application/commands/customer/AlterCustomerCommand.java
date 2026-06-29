package com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer;

import java.util.UUID;

public record AlterCustomerCommand(
        UUID id,
        String name,
        String email,
        String phone,
        String cnpjCpf
) {
}
