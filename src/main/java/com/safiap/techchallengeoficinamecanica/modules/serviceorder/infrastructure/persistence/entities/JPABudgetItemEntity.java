package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_items")
@Getter
@NoArgsConstructor
public class JPABudgetItemEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID budgetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetItemType type;

    @Column(nullable = false)
    private UUID itemId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public JPABudgetItemEntity(UUID id, UUID budgetId, BudgetItemType type, UUID itemId,
                               String description, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.budgetId = budgetId;
        this.type = type;
        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
