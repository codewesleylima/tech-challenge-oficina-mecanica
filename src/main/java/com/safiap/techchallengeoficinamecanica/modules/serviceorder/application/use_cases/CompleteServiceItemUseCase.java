package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompleteServiceItemUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;

    public CompleteServiceItemUseCase(ServiceOrderRepository serviceOrderRepository,
                                      BudgetRepository budgetRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public BudgetResponse execute(UUID serviceOrderId, UUID budgetItemId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));
        if (serviceOrder.getStatus() != ServiceOrderStatus.IN_EXECUTION)
            throw new ConflictException("Service order must be in IN_EXECUTION status to complete a service item");

        Budget budget = budgetRepository.findByServiceOrderId(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Budget not found for service order: " + serviceOrderId));
        budget.completeServiceItem(budgetItemId);
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }
}
