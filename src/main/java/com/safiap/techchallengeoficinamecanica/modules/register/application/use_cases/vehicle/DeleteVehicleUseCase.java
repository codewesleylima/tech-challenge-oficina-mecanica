package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteVehicleUseCase {


    private final VehicleRepository vehicleRepository;

    public DeleteVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void execute(UUID id){

        Vehicle vehicle = vehicleRepository.findByVehicleId(id).orElseThrow(() -> new DomainException("Vehicle with id: " + id + " not found"));

        vehicleRepository.delete(vehicle);
    }

}
