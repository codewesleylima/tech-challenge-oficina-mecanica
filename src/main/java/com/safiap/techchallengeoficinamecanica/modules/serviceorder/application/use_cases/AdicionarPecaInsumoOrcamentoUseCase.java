package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AdicionarPecaInsumoCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdicionarPecaInsumoOrcamentoUseCase {

    private final BudgetRepository budgetRepository;

    public AdicionarPecaInsumoOrcamentoUseCase(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public BudgetResponse execute(AdicionarPecaInsumoCommand command) {
        Budget budget = budgetRepository.findByServiceOrderId(command.serviceOrderId())
                .orElseThrow(() -> new NotFoundException("Budget not found for service order: " + command.serviceOrderId()));

        budget.addPart(command.itemId(), command.description(), command.quantity(), command.unitPrice());
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }
}
