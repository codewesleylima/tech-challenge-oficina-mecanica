package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities.JPAVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPAVehicleRepository extends JpaRepository<JPAVehicleEntity, UUID> {

    Optional<JPAVehicleEntity> findByCarLicensePlate(String carLicensePlate);
}
