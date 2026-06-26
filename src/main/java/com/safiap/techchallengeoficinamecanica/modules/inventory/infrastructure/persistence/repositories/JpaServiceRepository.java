package com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.entities.JpaServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaServiceRepository extends JpaRepository<JpaServiceEntity, UUID> {
}
