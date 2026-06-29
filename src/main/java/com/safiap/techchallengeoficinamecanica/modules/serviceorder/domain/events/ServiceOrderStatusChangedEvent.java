package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ServiceOrderStatusChangedEvent(
        UUID serviceOrderId,
        UUID customerId,
        UUID vehicleId,
        ServiceOrderStatus previousStatus,
        ServiceOrderStatus newStatus,
        Instant occurredOn
) implements DomainEvent {

    public static ServiceOrderStatusChangedEvent of(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                                                    ServiceOrderStatus previousStatus, ServiceOrderStatus newStatus) {
        return new ServiceOrderStatusChangedEvent(serviceOrderId, customerId, vehicleId,
                previousStatus, newStatus, Instant.now());
    }
}
