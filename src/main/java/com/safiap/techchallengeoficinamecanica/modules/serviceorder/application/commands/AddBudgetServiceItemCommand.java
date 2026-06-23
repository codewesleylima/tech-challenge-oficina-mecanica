package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AddBudgetServiceItemCommand(UUID serviceOrderId, String description, BigDecimal price) {}
