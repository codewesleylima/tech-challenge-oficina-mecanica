package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record DiagnosisConcludedEvent(UUID serviceOrderId, Instant occurredOn) implements DomainEvent {
    public DiagnosisConcludedEvent(UUID serviceOrderId) {
        this(serviceOrderId, Instant.now());
    }
}
