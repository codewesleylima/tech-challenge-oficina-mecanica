package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class BudgetItemPart extends Entity {

    private UUID budgetItemPartId;
    private UUID partId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;

    private BudgetItemPart() {}

    private BudgetItemPart(UUID budgetItemPartId, UUID partId, String description, Integer quantity, BigDecimal unitPrice) {
        this.budgetItemPartId = budgetItemPartId;
        this.partId = partId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static BudgetItemPart create(UUID partId, String description, Integer quantity, BigDecimal unitPrice) {
        Objects.requireNonNull(description, "description is null");
        Objects.requireNonNull(quantity, "quantity is null");
        Objects.requireNonNull(unitPrice, "unitPrice is null");
        if (quantity <= 0) throw new DomainException("Quantity must be greater than zero");
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) throw new DomainException("Unit price cannot be negative");
        return new BudgetItemPart(UUID.randomUUID(), partId, description, quantity, unitPrice);
    }

    public static BudgetItemPart build(UUID budgetItemPartId, UUID partId, String description, Integer quantity, BigDecimal unitPrice) {
        return new BudgetItemPart(budgetItemPartId, partId, description, quantity, unitPrice);
    }

    public UUID getBudgetItemPartId() { return budgetItemPartId; }
    public UUID getPartId() { return partId; }
    public String getDescription() { return description; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
