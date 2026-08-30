package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetItemsCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services.BudgetAssemblyService;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddItemsToBudgetUseCase {

    private final BudgetAssemblyService budgetAssemblyService;
    private final BudgetRepository budgetRepository;

    public AddItemsToBudgetUseCase(BudgetAssemblyService budgetAssemblyService,
                                   BudgetRepository budgetRepository) {
        this.budgetAssemblyService = budgetAssemblyService;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public BudgetResponse execute(AddBudgetItemsCommand command) {
        if (command.items() == null || command.items().isEmpty())
            throw new DomainException("At least one budget item is required");

        Budget budget = budgetAssemblyService.getOrCreate(command.serviceOrderId());
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }
}
