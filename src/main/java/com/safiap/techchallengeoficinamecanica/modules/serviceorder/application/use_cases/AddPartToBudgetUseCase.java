package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetItemsCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.BudgetItemInput;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddPartToBudgetUseCase {

    private final AddItemsToBudgetUseCase addItemsToBudgetUseCase;

    public AddPartToBudgetUseCase(AddItemsToBudgetUseCase addItemsToBudgetUseCase) {
        this.addItemsToBudgetUseCase = addItemsToBudgetUseCase;
    }

    public BudgetResponse execute(AddPartCommand command) {
        BudgetItemInput part = new BudgetItemInput(
                BudgetItemType.PART, command.itemId(), command.description(), command.quantity());
        return addItemsToBudgetUseCase.execute(
                new AddBudgetItemsCommand(command.serviceOrderId(), List.of(part)));
    }
}
