package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.GetVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAllVehiclesByCustomerIdUseCase {

    private final VehicleRepository vehicleRepository;

    public GetAllVehiclesByCustomerIdUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<GetVehicleResponse> execute(UUID customerId){
        return vehicleRepository
                .findByCustomerId(customerId)
                .stream()
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
                ).toList();
    }
}
