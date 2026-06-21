package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities.JPAVehicleEntity;

public class VehicleMapper {

    public static JPAVehicleEntity toJPA(Vehicle vehicle) {
        return new JPAVehicleEntity(
                 vehicle.getVehicleId(),
                 vehicle.getCustomerId(),
                 vehicle.getCarLicensePlate().plate(),
                 vehicle.getModel(),
                 vehicle.getManufacturer(),
                 vehicle.getKilometers().value(),
                 vehicle.getYear()
        );
    }

    public static Vehicle toEntity(JPAVehicleEntity vehicleEntity) {
        return Vehicle.buildVehicle(
                vehicleEntity.getId(),
                vehicleEntity.getCustomerId(),
                new CarLicensePlate(vehicleEntity.getCarLicensePlate()),
                vehicleEntity.getModel(),
                vehicleEntity.getManufacturer(),
                new Kilometers(vehicleEntity.getKilometers()),
                vehicleEntity.getYear()
        );
    }

}
