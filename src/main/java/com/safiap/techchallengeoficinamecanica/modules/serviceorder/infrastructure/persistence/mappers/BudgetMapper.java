package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.BudgetItem;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPABudgetEntity;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPABudgetItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetMapper {

    public static JPABudgetEntity toJPA(Budget budget) {
        return new JPABudgetEntity(
                budget.getBudgetId(),
                budget.getServiceOrderId(),
                budget.getStatus(),
                budget.getCreatedAt()
        );
    }

    public static JPABudgetItemEntity itemToJPA(BudgetItem item) {
        return new JPABudgetItemEntity(
                item.getBudgetItemId(),
                item.getBudgetId(),
                item.getType(),
                item.getItemId(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getCompletedAt()
        );
    }

    public static Budget toEntity(JPABudgetEntity entity, List<JPABudgetItemEntity> itemEntities) {
        List<BudgetItem> items = itemEntities.stream()
                .map(i -> BudgetItem.build(i.getId(), i.getBudgetId(), i.getType(),
                        i.getItemId(), i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getCompletedAt()))
                .collect(Collectors.toList());
        return Budget.build(entity.getId(), entity.getServiceOrderId(), entity.getStatus(),
                items, entity.getCreatedAt());
    }
}
