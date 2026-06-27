package com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.DTO.part;

import java.math.BigDecimal;

public record RegisterPartDTO(
        String name, String description, int quantity, BigDecimal price
) {
}
