package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

public record ServiceOrderWithBudgetResponse(
        ServiceOrderResponse serviceOrder,
        BudgetResponse budget
) {
    public static ServiceOrderWithBudgetResponse of(ServiceOrderResponse serviceOrder, BudgetResponse budget) {
        return new ServiceOrderWithBudgetResponse(serviceOrder, budget);
    }
}