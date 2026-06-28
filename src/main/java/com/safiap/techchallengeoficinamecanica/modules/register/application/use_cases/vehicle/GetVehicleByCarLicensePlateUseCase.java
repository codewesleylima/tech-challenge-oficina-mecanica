package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.GetVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetVehicleByCarLicensePlateUseCase {

    private final VehicleRepository vehicleRepository;

    public GetVehicleByCarLicensePlateUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public GetVehicleResponse execute(String plate){
        return vehicleRepository
                .findByCarLicensePlate(new CarLicensePlate(plate))
                .map(vehicle ->
                        new GetVehicleResponse(
                                vehicle.getVehicleId(),
                                vehicle.getCustomerId(),
                                vehicle.getCarLicensePlate().plate(),
                                vehicle.getModel(),
                                vehicle.getManufacturer(),
                                vehicle.getKilometers().value(),
                                vehicle.getYear()
                        )
                ).orElseThrow(
                        () -> new DomainException("Vehicle with Plate " + plate + " not found.")
                );
    }
}
