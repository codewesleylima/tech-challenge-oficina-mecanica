package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.GetVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetVehicleByIdUseCase {

    private final VehicleRepository vehicleRepository;

    public GetVehicleByIdUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public GetVehicleResponse execute(UUID id){
        return vehicleRepository
                .findByVehicleId(id)
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
                        () -> new DomainException("Vehicle with id " + id + " not found.")
                );
    }
}
