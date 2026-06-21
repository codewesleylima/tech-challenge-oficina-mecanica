package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.impl;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mapper.VehicleMapper;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repository.VehicleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;

    public VehicleRepositoryImpl(VehicleJpaRepository vehicleJpaRepository) {
        this.vehicleJpaRepository = vehicleJpaRepository;
    }

    @Override
    public void save(Vehicle vehicle) {
        vehicleJpaRepository.save(VehicleMapper.toEntity(vehicle));
    }

    @Override
    public Optional<Vehicle> findByVehicleId(UUID vehicleId) {
        return vehicleJpaRepository.findById(vehicleId).map(VehicleMapper::toDomain);
    }

    @Override
    public Optional<Vehicle> findByCarLicensePlate(CarLicensePlate carLicensePlate) {
        return vehicleJpaRepository.findByCarLicensePlateEntity(carLicensePlate.plate()).map(VehicleMapper::toDomain);
    }
}
