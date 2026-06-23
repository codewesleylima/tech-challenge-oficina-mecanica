package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Budget extends Entity {

    private UUID budgetId;
    private BudgetStatus status;
    private List<BudgetItemPart> partItems;
    private List<BudgetItemService> serviceItems;

    private Budget() {}

    private Budget(UUID budgetId, BudgetStatus status, List<BudgetItemPart> partItems, List<BudgetItemService> serviceItems) {
        this.budgetId = budgetId;
        this.status = status;
        this.partItems = new ArrayList<>(partItems);
        this.serviceItems = new ArrayList<>(serviceItems);
    }

    public static Budget create() {
        return new Budget(UUID.randomUUID(), BudgetStatus.DRAFT, new ArrayList<>(), new ArrayList<>());
    }

    public static Budget build(UUID budgetId, BudgetStatus status, List<BudgetItemPart> partItems, List<BudgetItemService> serviceItems) {
        Objects.requireNonNull(budgetId, "budgetId is null");
        Objects.requireNonNull(status, "status is null");
        return new Budget(budgetId, status, partItems != null ? partItems : new ArrayList<>(), serviceItems != null ? serviceItems : new ArrayList<>());
    }

    public void addPartItem(BudgetItemPart item) {
        if (status != BudgetStatus.DRAFT) throw new DomainException("Cannot add items to a non-draft budget");
        partItems.add(item);
    }

    public void addServiceItem(BudgetItemService item) {
        if (status != BudgetStatus.DRAFT) throw new DomainException("Cannot add items to a non-draft budget");
        serviceItems.add(item);
    }

    public void approve() {
        if (status != BudgetStatus.DRAFT) throw new DomainException("Only a draft budget can be approved");
        if (partItems.isEmpty() && serviceItems.isEmpty()) throw new DomainException("Budget must have at least one item to be approved");
        this.status = BudgetStatus.APPROVED;
    }

    public void reject() {
        if (status != BudgetStatus.DRAFT) throw new DomainException("Only a draft budget can be rejected");
        this.status = BudgetStatus.REJECTED;
    }

    public BigDecimal getTotalValue() {
        BigDecimal partsTotal = partItems.stream()
                .map(BudgetItemPart::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal servicesTotal = serviceItems.stream()
                .map(BudgetItemService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return partsTotal.add(servicesTotal);
    }

    public UUID getBudgetId() { return budgetId; }
    public BudgetStatus getStatus() { return status; }
    public List<BudgetItemPart> getPartItems() { return Collections.unmodifiableList(partItems); }
    public List<BudgetItemService> getServiceItems() { return Collections.unmodifiableList(serviceItems); }
}
