package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID serviceOrderId,
        UUID customerId,
        UUID vehicleId,
        String problemDescription,
        ServiceOrderStatus status,
        LocalDateTime openedAt,
        LocalDateTime concludedAt
) {
}
