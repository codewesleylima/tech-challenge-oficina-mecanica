package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RejectBudgetUseCase {

    private static final Logger log = LoggerFactory.getLogger(RejectBudgetUseCase.class);

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RejectBudgetUseCase(ServiceOrderRepository serviceOrderRepository,
                               BudgetRepository budgetRepository,
                               DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        serviceOrder.rejectBudget();
        serviceOrderRepository.save(serviceOrder);

        // reabre o orçamento para revisão, já que a OS volta para o diagnóstico
        budgetRepository.findByServiceOrderId(serviceOrderId).ifPresent(budget -> {
            budget.reopen();
            budgetRepository.save(budget);
        });

        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        log.warn("Budget rejected by customer for service order {} - returned to diagnosis", serviceOrderId);

        return ServiceOrderResponse.from(serviceOrder);
    }
}
