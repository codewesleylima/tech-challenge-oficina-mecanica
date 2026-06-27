package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "budgets")
@Getter
@NoArgsConstructor
public class JPABudgetEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID serviceOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public JPABudgetEntity(UUID id, UUID serviceOrderId, BudgetStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.serviceOrderId = serviceOrderId;
        this.status = status;
        this.createdAt = createdAt;
    }
}
