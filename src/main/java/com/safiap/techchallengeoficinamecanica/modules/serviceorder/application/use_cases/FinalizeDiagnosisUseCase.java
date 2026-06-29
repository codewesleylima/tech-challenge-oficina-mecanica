package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.Diagnosis;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FinalizeDiagnosisUseCase {

    private static final Logger log = LoggerFactory.getLogger(FinalizeDiagnosisUseCase.class);

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;
    private final DomainEventPublisher domainEventPublisher;

    public FinalizeDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository,
                                    BudgetRepository budgetRepository,
                                    DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId, String diagnosis) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        Budget budget = budgetRepository.findByServiceOrderId(serviceOrderId)
                .filter(b -> b.getStatus() == BudgetStatus.FINALIZED && !b.getItems().isEmpty())
                .orElseThrow(() -> new ConflictException("A finalized budget with items is required before closing diagnosis"));

        serviceOrder.finalizeDiagnosis(new Diagnosis(diagnosis));
        serviceOrderRepository.save(serviceOrder);
        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        log.info("Service order {} awaiting customer approval - final budget total {} ({} item(s))",
                serviceOrderId, budget.calculateTotal(), budget.getItems().size());

        return ServiceOrderResponse.from(serviceOrder);
    }
}
