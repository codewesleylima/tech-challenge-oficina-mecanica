package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CalcularOrcamentoUseCase {

    private final BudgetRepository budgetRepository;

    public CalcularOrcamentoUseCase(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public BudgetResponse execute(UUID serviceOrderId) {
        return budgetRepository.findByServiceOrderId(serviceOrderId)
                .map(BudgetResponse::from)
                .orElseThrow(() -> new NotFoundException("Budget not found for service order: " + serviceOrderId));
    }
}
