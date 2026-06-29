package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.PartStockPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StartServiceOrderExecutionUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;
    private final PartStockPort partStockPort;
    private final DomainEventPublisher domainEventPublisher;

    public StartServiceOrderExecutionUseCase(ServiceOrderRepository serviceOrderRepository,
                                             BudgetRepository budgetRepository,
                                             PartStockPort partStockPort,
                                             DomainEventPublisher domainEventPublisher) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
        this.partStockPort = partStockPort;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        serviceOrder.startExecution();
        consumePartStock(serviceOrderId);
        serviceOrderRepository.save(serviceOrder);
        domainEventPublisher.publishAll(serviceOrder.pullDomainEvents());

        return ServiceOrderResponse.from(serviceOrder);
    }

    private void consumePartStock(UUID serviceOrderId) {
        budgetRepository.findByServiceOrderId(serviceOrderId)
                .map(Budget::getItems)
                .ifPresent(items -> items.stream()
                        .filter(item -> item.getType() == BudgetItemType.PART)
                        .forEach(item -> partStockPort.decreaseStock(item.getItemId(), item.getQuantity())));
    }
}
