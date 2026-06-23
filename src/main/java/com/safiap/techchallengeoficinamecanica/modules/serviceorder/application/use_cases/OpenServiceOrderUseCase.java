package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final DomainEventPublisher eventPublisher;

    public OpenServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository, DomainEventPublisher eventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(OpenServiceOrderCommand command) {
        ServiceOrder serviceOrder = ServiceOrder.open(command.customerId(), command.vehicleId(), command.problemDescription());
        serviceOrderRepository.save(serviceOrder);
        eventPublisher.publishAll(serviceOrder.pullDomainEvents());
        return toResponse(serviceOrder);
    }

    private ServiceOrderResponse toResponse(ServiceOrder so) {
        return new ServiceOrderResponse(so.getServiceOrderId(), so.getCustomerId(), so.getVehicleId(),
                so.getProblemDescription(), so.getStatus().name(), so.getOpenedAt(), so.getConcludedAt());
    }
}
