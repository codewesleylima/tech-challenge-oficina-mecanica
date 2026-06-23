package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_items_service")
@Getter
@NoArgsConstructor
public class JPABudgetItemServiceEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public JPABudgetItemServiceEntity(UUID id, String description, BigDecimal price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }
}
