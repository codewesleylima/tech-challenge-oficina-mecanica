package com.safiap.techchallengeoficinamecanica.modules.shared.domain.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {

    void publish(DomainEvent domainEvent);
    void publishAll(List<DomainEvent> domainEvents);
}
