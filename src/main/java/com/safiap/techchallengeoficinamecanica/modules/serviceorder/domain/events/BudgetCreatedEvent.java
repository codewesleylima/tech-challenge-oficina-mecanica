package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record BudgetCreatedEvent(UUID serviceOrderId, UUID budgetId, Instant occurredOn) implements DomainEvent {
    public BudgetCreatedEvent(UUID serviceOrderId, UUID budgetId) {
        this(serviceOrderId, budgetId, Instant.now());
    }
}
