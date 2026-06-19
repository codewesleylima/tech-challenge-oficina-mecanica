package com.safiap.techchallengeoficinamecanica.modules.shared.infrastructure.events;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.DomainEvent;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
    }

    @Override
    public void publishAll(List<DomainEvent> domainEvents) {
        domainEvents.forEach(this::publish);
    }
}
