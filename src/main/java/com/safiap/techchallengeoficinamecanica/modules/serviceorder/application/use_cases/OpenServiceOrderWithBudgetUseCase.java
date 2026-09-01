package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetItemsCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderWithBudgetCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderWithBudgetResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenServiceOrderWithBudgetUseCase {

    private final OpenServiceOrderUseCase openServiceOrderUseCase;
    private final StartDiagnosisUseCase startDiagnosisUseCase;
    private final AddItemsToBudgetUseCase addItemsToBudgetUseCase;

    public OpenServiceOrderWithBudgetUseCase(OpenServiceOrderUseCase openServiceOrderUseCase,
                                             StartDiagnosisUseCase startDiagnosisUseCase,
                                             AddItemsToBudgetUseCase addItemsToBudgetUseCase) {
        this.openServiceOrderUseCase = openServiceOrderUseCase;
        this.startDiagnosisUseCase = startDiagnosisUseCase;
        this.addItemsToBudgetUseCase = addItemsToBudgetUseCase;
    }

    @Transactional
    public ServiceOrderWithBudgetResponse execute(OpenServiceOrderWithBudgetCommand command) {
        ServiceOrderResponse opened = openServiceOrderUseCase.execute(
                new OpenServiceOrderCommand(
                        command.customerId(), command.vehicleId(), command.problemDescription())
        );

        ServiceOrderResponse inDiagnosis = startDiagnosisUseCase.execute(opened.serviceOrderId());

        BudgetResponse budgetResponse = addItemsToBudgetUseCase.execute(
                new AddBudgetItemsCommand(inDiagnosis.serviceOrderId(), command.items())
        );

        return ServiceOrderWithBudgetResponse.of(inDiagnosis, budgetResponse);
    }
}