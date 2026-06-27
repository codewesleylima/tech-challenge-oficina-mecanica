package com.safiap.techchallengeoficinamecanica.modules.inventory.presentation.DTO.service;

import java.math.BigDecimal;

public record RegisterServiceDTO(
        String name, String description, BigDecimal price
) {
}
