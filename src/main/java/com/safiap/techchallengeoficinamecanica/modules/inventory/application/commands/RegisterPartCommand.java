package com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands;

import java.math.BigDecimal;

public record RegisterPartCommand(
        String name, String description, int quantity, BigDecimal price
) {
}
