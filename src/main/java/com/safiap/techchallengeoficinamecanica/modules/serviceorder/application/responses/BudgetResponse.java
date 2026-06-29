package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record BudgetResponse(
        UUID budgetId,
        UUID serviceOrderId,
        BudgetStatus status,
        List<BudgetItemResponse> items,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
    public record BudgetItemResponse(
            UUID budgetItemId,
            String type,
            UUID itemId,
            String description,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            LocalDateTime completedAt
    ) {}

    public static BudgetResponse from(Budget budget) {
        List<BudgetItemResponse> items = budget.getItems().stream()
                .map(i -> new BudgetItemResponse(
                        i.getBudgetItemId(), i.getType().name(), i.getItemId(),
                        i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.totalPrice(),
                        i.getCompletedAt()))
                .collect(Collectors.toList());
        return new BudgetResponse(
                budget.getBudgetId(), budget.getServiceOrderId(), budget.getStatus(),
                items, budget.calculateTotal(), budget.getCreatedAt()
        );
    }
}
