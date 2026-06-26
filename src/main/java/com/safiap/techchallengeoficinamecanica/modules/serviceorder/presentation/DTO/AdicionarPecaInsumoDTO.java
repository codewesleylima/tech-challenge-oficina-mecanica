package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record AdicionarPecaInsumoDTO(
        UUID itemId,
        String description,
        int quantity,
        BigDecimal unitPrice
) {}
