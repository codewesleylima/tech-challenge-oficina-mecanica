package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IniciarOrcamentoUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;

    public IniciarOrcamentoUseCase(ServiceOrderRepository serviceOrderRepository,
                                   BudgetRepository budgetRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public BudgetResponse execute(UUID serviceOrderId) {
        serviceOrderRepository.findById(serviceOrderId)
                .filter(os -> os.getStatus() == ServiceOrderStatus.IN_DIAGNOSIS)
                .orElseThrow(() -> new NotFoundException("Service order not found or not in IN_DIAGNOSIS status: " + serviceOrderId));

        if (budgetRepository.findByServiceOrderId(serviceOrderId).isPresent())
            throw new ConflictException("A budget already exists for this service order");

        Budget budget = Budget.create(serviceOrderId);
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }
}
