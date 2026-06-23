package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class BudgetItemService extends Entity {

    private UUID budgetItemServiceId;
    private String description;
    private BigDecimal price;

    private BudgetItemService() {}

    private BudgetItemService(UUID budgetItemServiceId, String description, BigDecimal price) {
        this.budgetItemServiceId = budgetItemServiceId;
        this.description = description;
        this.price = price;
    }

    public static BudgetItemService create(String description, BigDecimal price) {
        Objects.requireNonNull(description, "description is null");
        Objects.requireNonNull(price, "price is null");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new DomainException("Price cannot be negative");
        return new BudgetItemService(UUID.randomUUID(), description, price);
    }

    public static BudgetItemService build(UUID budgetItemServiceId, String description, BigDecimal price) {
        return new BudgetItemService(budgetItemServiceId, description, price);
    }

    public UUID getBudgetItemServiceId() { return budgetItemServiceId; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
}
