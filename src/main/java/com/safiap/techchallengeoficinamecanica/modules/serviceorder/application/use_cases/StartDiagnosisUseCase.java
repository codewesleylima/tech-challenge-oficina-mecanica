package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.StartDiagnosisCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StartDiagnosisUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final DomainEventPublisher eventPublisher;

    public StartDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository, DomainEventPublisher eventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(StartDiagnosisCommand command) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(command.serviceOrderId())
                .orElseThrow(() -> new DomainException("Service order not found"));
        serviceOrder.startDiagnosis();
        serviceOrderRepository.save(serviceOrder);
        eventPublisher.publishAll(serviceOrder.pullDomainEvents());
        return toResponse(serviceOrder);
    }

    private ServiceOrderResponse toResponse(ServiceOrder so) {
        return new ServiceOrderResponse(so.getServiceOrderId(), so.getCustomerId(), so.getVehicleId(),
                so.getProblemDescription(), so.getStatus().name(), so.getOpenedAt(), so.getConcludedAt());
    }
}
