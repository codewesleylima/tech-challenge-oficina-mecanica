package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPABudgetItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JPABudgetItemRepository extends JpaRepository<JPABudgetItemEntity, UUID> {
    List<JPABudgetItemEntity> findByBudgetId(UUID budgetId);
    void deleteByBudgetId(UUID budgetId);
}
