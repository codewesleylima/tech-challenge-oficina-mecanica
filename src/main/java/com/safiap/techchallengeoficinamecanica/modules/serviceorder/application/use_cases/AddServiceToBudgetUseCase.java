package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetItemsCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.BudgetItemInput;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddServiceToBudgetUseCase {

    private final AddItemsToBudgetUseCase addItemsToBudgetUseCase;

    public AddServiceToBudgetUseCase(AddItemsToBudgetUseCase addItemsToBudgetUseCase) {
        this.addItemsToBudgetUseCase = addItemsToBudgetUseCase;
    }

    public BudgetResponse execute(AddServiceCommand command) {
        BudgetItemInput service = new BudgetItemInput(
                BudgetItemType.SERVICE, command.itemId(), command.description(), command.quantity());
        return addItemsToBudgetUseCase.execute(
                new AddBudgetItemsCommand(command.serviceOrderId(), List.of(service)));
    }
}
