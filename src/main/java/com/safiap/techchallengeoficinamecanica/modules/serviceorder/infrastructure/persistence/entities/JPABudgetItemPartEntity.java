package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_items_part")
@Getter
@NoArgsConstructor
public class JPABudgetItemPartEntity {

    @Id
    private UUID id;

    @Column(name = "part_id")
    private UUID partId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public JPABudgetItemPartEntity(UUID id, UUID partId, String description, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.partId = partId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
