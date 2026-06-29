package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BudgetItem {

    private UUID budgetItemId;
    private UUID budgetId;
    private BudgetItemType type;
    private UUID itemId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private LocalDateTime completedAt;

    private BudgetItem(UUID budgetItemId, UUID budgetId, BudgetItemType type,
                       UUID itemId, String description, int quantity, BigDecimal unitPrice,
                       LocalDateTime completedAt) {
        this.budgetItemId = budgetItemId;
        this.budgetId = budgetId;
        this.type = type;
        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.completedAt = completedAt;
    }

    public static BudgetItem create(UUID budgetId, BudgetItemType type, UUID itemId,
                                    String description, int quantity, BigDecimal unitPrice) {
        return new BudgetItem(UUID.randomUUID(), budgetId, type, itemId, description, quantity, unitPrice, null);
    }

    public static BudgetItem build(UUID budgetItemId, UUID budgetId, BudgetItemType type,
                                   UUID itemId, String description, int quantity, BigDecimal unitPrice,
                                   LocalDateTime completedAt) {
        return new BudgetItem(budgetItemId, budgetId, type, itemId, description, quantity, unitPrice, completedAt);
    }

    public void complete() {
        if (type != BudgetItemType.SERVICE)
            throw new ConflictException("Only service items can be completed");
        if (completedAt != null)
            throw new ConflictException("Service item is already completed");
        this.completedAt = LocalDateTime.now();
    }

    public boolean isService() {
        return type == BudgetItemType.SERVICE;
    }

    public boolean isCompleted() {
        return completedAt != null;
    }

    public BigDecimal totalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public UUID getBudgetItemId() { return budgetItemId; }
    public UUID getBudgetId() { return budgetId; }
    public BudgetItemType getType() { return type; }
    public UUID getItemId() { return itemId; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
