package com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part;

import java.math.BigDecimal;
import java.util.UUID;

public record AlterPartResponse(
        UUID id, String name, String description, int quantity, BigDecimal price
) {
}
