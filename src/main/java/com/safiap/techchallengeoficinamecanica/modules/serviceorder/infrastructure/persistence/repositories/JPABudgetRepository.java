package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPABudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JPABudgetRepository extends JpaRepository<JPABudgetEntity, UUID> {
    Optional<JPABudgetEntity> findByServiceOrderId(UUID serviceOrderId);
}
