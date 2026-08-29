package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.FinalizeDiagnosisCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services.BudgetAssemblyService;
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

@Service
public class FinalizeDiagnosisUseCase {

    private static final Logger log = LoggerFactory.getLogger(FinalizeDiagnosisUseCase.class);

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetAssemblyService budgetAssemblyService;
    private final DomainEventPublisher domainEventPublisher;

    public FinalizeDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository,
                                    BudgetRepository budgetRepository,
                                    BudgetAssemblyService budgetAssemblyService,
                                    DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
        this.budgetAssemblyService = budgetAssemblyService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(FinalizeDiagnosisCommand command) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(command.serviceOrderId())
                .orElseThrow(() -> new NotFoundException("Service order not found: " + command.serviceOrderId()));

        Budget budget = budgetAssemblyService.getOrCreate(serviceOrder);
        budgetAssemblyService.addItems(budget, command.items());

        if (budget.getItems().isEmpty())
            throw new ConflictException("A budget with items is required before closing diagnosis");

        if (budget.getStatus() != BudgetStatus.FINALIZED)
            budget.finalize();
        budgetRepository.save(budget);

        serviceOrder.finalizeDiagnosis(new Diagnosis(command.diagnosis()));
        serviceOrderRepository.save(serviceOrder);
        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        log.info("Service order {} awaiting customer approval - final budget total {} ({} item(s))",
                command.serviceOrderId(), budget.calculateTotal(), budget.getItems().size());

        return ServiceOrderResponse.from(serviceOrder);
    }
}
