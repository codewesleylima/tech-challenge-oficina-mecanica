package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AddServiceToBudgetUseCase {

    private final BudgetRepository budgetRepository;
    private final InventoryCatalogPort inventoryCatalogPort;

    public AddServiceToBudgetUseCase(BudgetRepository budgetRepository,
                                     InventoryCatalogPort inventoryCatalogPort) {
        this.budgetRepository = budgetRepository;
        this.inventoryCatalogPort = inventoryCatalogPort;
    }

    @Transactional
    public BudgetResponse execute(AddServiceCommand command) {
        Budget budget = budgetRepository.findByServiceOrderId(command.serviceOrderId())
                .orElseThrow(() -> new NotFoundException("Budget not found for service order: " + command.serviceOrderId()));

        BigDecimal unitPrice = inventoryCatalogPort.getServicePrice(command.itemId());

        budget.addService(command.itemId(), command.description(), command.quantity(), unitPrice);
        budgetRepository.save(budget);

        return BudgetResponse.from(budget);
    }
}
