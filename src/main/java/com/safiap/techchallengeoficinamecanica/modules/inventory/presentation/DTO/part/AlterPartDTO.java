package com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.DTO.part;

import java.math.BigDecimal;

public record AlterPartDTO(
        String name, String description, BigDecimal price
) {
}
