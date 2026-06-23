package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "budgets")
@Getter
@NoArgsConstructor
public class JPABudgetEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "budget_id")
    private List<JPABudgetItemPartEntity> partItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "budget_id")
    private List<JPABudgetItemServiceEntity> serviceItems = new ArrayList<>();

    public JPABudgetEntity(UUID id, BudgetStatus status, List<JPABudgetItemPartEntity> partItems, List<JPABudgetItemServiceEntity> serviceItems) {
        this.id = id;
        this.status = status;
        this.partItems = partItems != null ? partItems : new ArrayList<>();
        this.serviceItems = serviceItems != null ? serviceItems : new ArrayList<>();
    }
}
