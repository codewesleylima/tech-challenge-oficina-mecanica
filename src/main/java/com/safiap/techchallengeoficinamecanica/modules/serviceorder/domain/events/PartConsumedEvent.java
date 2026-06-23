package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record PartConsumedEvent(UUID serviceOrderId, UUID partId, Integer quantity, Instant occurredOn) implements DomainEvent {
    public PartConsumedEvent(UUID serviceOrderId, UUID partId, Integer quantity) {
        this(serviceOrderId, partId, quantity, Instant.now());
    }
}
