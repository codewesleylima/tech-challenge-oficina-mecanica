package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ServiceOrderOpenedEvent(UUID serviceOrderId, UUID customerId, UUID vehicleId, Instant occurredOn) implements DomainEvent {
    public ServiceOrderOpenedEvent(UUID serviceOrderId, UUID customerId, UUID vehicleId) {
        this(serviceOrderId, customerId, vehicleId, Instant.now());
    }
}
