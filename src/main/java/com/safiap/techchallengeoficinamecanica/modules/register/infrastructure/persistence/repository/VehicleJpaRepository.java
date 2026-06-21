package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repository;

import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {

    Optional<VehicleEntity> findByCarLicensePlateEntity(String carLicensePlate);
}
