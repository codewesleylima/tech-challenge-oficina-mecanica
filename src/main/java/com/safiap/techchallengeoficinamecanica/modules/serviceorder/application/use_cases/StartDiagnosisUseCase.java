package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StartDiagnosisUseCase {

    private static final Logger log = LoggerFactory.getLogger(StartDiagnosisUseCase.class);

    private final ServiceOrderRepository serviceOrderRepository;
    private final DomainEventPublisher domainEventPublisher;

    public StartDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository,
                                 DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        serviceOrder.startDiagnosis();
        serviceOrderRepository.save(serviceOrder);
        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        log.debug("Service order {} moved to IN_DIAGNOSIS", serviceOrderId);

        return ServiceOrderResponse.from(serviceOrder);
    }
}
