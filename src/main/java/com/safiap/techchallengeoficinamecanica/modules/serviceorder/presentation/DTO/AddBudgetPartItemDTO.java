package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record AddBudgetPartItemDTO(UUID partId, String description, Integer quantity, BigDecimal unitPrice) {}
