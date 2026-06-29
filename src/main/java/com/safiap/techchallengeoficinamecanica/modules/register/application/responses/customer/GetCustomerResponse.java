package com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer;

import java.util.UUID;

public record GetCustomerResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String cnpjCpf
) {
}
