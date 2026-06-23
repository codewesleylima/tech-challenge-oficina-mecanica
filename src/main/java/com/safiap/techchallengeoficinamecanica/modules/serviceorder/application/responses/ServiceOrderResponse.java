package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID serviceOrderId,
        UUID customerId,
        UUID vehicleId,
        String problemDescription,
        String status,
        LocalDateTime openedAt,
        LocalDateTime concludedAt
) {}
