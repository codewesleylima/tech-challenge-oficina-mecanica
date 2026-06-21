package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mapper;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity.VehicleEntity;

import java.time.Year;

public class VehicleMapper {

    public static VehicleEntity toEntity(Vehicle vehicle) {
        return new VehicleEntity(
                vehicle.getVehicleId(),
                vehicle.getCustomerId(),
                vehicle.getCarLicensePlate().plate(),
                vehicle.getModel(),
                vehicle.getManufacturer(),
                vehicle.getKilometers().value(),
                vehicle.getYear().getValue()
        );
    }

    public static Vehicle toDomain(VehicleEntity entity) {
        return Vehicle.buildVehicle(
                entity.getVehicleIdEntity(),
                entity.getCustomerIdEntity(),
                new CarLicensePlate(entity.getCarLicensePlateEntity()),
                entity.getModelEntity(),
                entity.getManufacturerEntity(),
                new Kilometers(entity.getKilometersEntity()),
                Year.of(entity.getYearEntity())
        );
    }
}
