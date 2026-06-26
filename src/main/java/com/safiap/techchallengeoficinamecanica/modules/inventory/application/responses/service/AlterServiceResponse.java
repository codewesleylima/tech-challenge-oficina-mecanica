package com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service;

import java.math.BigDecimal;
import java.util.UUID;

public record AlterServiceResponse(
        UUID id, String name, String description, BigDecimal price
) {
}
