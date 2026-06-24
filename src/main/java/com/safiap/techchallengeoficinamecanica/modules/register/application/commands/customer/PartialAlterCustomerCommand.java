package com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer;

import java.util.UUID;

public record PartialAlterCustomerCommand(
        UUID id,
        String Name,
        String Email,
        String Phone,
        String Cpf
) {
}
