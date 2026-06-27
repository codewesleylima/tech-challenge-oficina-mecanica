package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPAServiceTimeRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JPAServiceTimeRecordRepository extends JpaRepository<JPAServiceTimeRecordEntity, UUID> {
    List<JPAServiceTimeRecordEntity> findByServiceOrderId(UUID serviceOrderId);
}
