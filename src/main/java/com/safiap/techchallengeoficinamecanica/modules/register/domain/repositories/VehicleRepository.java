package com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
    void save(Vehicle vehicle);
    Optional<Vehicle> findByVehicleId(UUID vehicleId);
    Optional<Vehicle> findByCarLicensePlate(CarLicensePlate carLicensePlate);
}
