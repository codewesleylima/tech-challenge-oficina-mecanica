package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AddBudgetPartItemCommand(UUID serviceOrderId, UUID partId, String description, Integer quantity, BigDecimal unitPrice) {}
