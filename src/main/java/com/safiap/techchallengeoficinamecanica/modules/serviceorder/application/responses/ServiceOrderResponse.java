package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID serviceOrderId,
        UUID customerId,
        UUID vehicleId,
        String problemDescription,
        String diagnosis,
        ServiceOrderStatus status,
        LocalDateTime openedAt,
        LocalDateTime executionStartedAt,
        LocalDateTime concludedAt,
        String priority
) {
    public static ServiceOrderResponse from(ServiceOrder serviceOrder) {
        return new ServiceOrderResponse(
                serviceOrder.getServiceOrderId(),
                serviceOrder.getCustomerId(),
                serviceOrder.getVehicleId(),
                serviceOrder.getProblemDescription(),
                serviceOrder.getDiagnosis() != null ? serviceOrder.getDiagnosis().value() : null,
                serviceOrder.getStatus(),
                serviceOrder.getOpenedAt(),
                serviceOrder.getExecutionStartedAt(),
                serviceOrder.getConcludedAt(),
                serviceOrder.getPriority().name()
        );
    }
}
