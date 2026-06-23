package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ServiceOrderCancelledEvent(UUID serviceOrderId, Instant occurredOn) implements DomainEvent {
    public ServiceOrderCancelledEvent(UUID serviceOrderId) {
        this(serviceOrderId, Instant.now());
    }
}
