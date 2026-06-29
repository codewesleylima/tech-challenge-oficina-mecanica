package com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer;

import java.util.UUID;

public record AlterCustomerResponse(UUID id, String Name, String Email, String Phone, String Cpf) {
}
