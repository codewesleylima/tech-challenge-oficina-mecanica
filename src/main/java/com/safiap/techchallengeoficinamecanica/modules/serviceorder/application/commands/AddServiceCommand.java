package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AddServiceCommand(
        UUID serviceOrderId,
        UUID itemId,
        String description,
        int quantity,
        BigDecimal unitPrice
) {}
