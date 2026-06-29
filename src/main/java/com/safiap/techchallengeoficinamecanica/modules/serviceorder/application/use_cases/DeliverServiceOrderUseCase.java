package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeliverServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final DomainEventPublisher domainEventPublisher;

    public DeliverServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository,
                                      DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        serviceOrder.deliver();
        serviceOrderRepository.save(serviceOrder);
        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        return ServiceOrderResponse.from(serviceOrder);
    }
}
