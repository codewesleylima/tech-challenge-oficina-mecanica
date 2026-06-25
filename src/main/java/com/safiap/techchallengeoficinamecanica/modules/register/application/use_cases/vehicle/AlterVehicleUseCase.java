package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.vehicle.AlterVehicleCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.AlterVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class AlterVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public AlterVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public AlterVehicleResponse execute(AlterVehicleCommand command) {

        Vehicle vehicle = vehicleRepository.findByVehicleId(command.vehicleId())
                .orElseThrow( () -> new DomainException("Vehicle not found."));

        vehicle.alterVehicle(
                new CarLicensePlate(command.carLicensePlate()),
                command.model(),
                command.manufactures(),
                new Kilometers(command.kilometers()),
                command.year()
        );

        vehicleRepository.save(vehicle);

        return new AlterVehicleResponse(
                vehicle.getVehicleId(),
                vehicle.getCustomerId(),
                vehicle.getCarLicensePlate().plate(),
                vehicle.getModel(),
                vehicle.getManufacturer(),
                vehicle.getKilometers().value(),
                vehicle.getYear()
        );

    }
}
