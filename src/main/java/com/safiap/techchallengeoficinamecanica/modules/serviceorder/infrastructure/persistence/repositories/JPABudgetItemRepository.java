package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPABudgetItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JPABudgetItemRepository extends JpaRepository<JPABudgetItemEntity, UUID> {
    List<JPABudgetItemEntity> findByBudgetId(UUID budgetId);
    void deleteByBudgetId(UUID budgetId);


    @Query("""
            select i.itemId as serviceId, so.executionStartedAt as startedAt, i.completedAt as completedAt
            from JPABudgetItemEntity i, JPABudgetEntity b, JPAServiceOrderEntity so
            where b.id = i.budgetId
              and so.id = b.serviceOrderId
              and b.serviceOrderId = :serviceOrderId
              and i.type = :type
              and i.completedAt is not null
              and so.executionStartedAt is not null
            """)
    List<ServiceTimeRow> findCompletedServiceTimes(@Param("serviceOrderId") UUID serviceOrderId,
                                                   @Param("type") BudgetItemType type);
}
