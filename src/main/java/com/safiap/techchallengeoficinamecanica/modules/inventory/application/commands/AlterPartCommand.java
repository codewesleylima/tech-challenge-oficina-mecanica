package com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands;

import java.math.BigDecimal;

public record AlterPartCommand(
        String name, String description, BigDecimal price
) {
}
