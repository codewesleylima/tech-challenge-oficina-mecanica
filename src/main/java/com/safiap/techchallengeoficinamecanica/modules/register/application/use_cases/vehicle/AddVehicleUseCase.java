package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.vehicle.AddVehicleCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.AddVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddVehicleUseCase {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public AddVehicleUseCase(CustomerRepository customerRepository, VehicleRepository vehicleRepository, DomainEventPublisher domainEventPublisher) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public AddVehicleResponse execute(AddVehicleCommand request){

        customerRepository.findByCustomerId(request.customerId()).orElseThrow(() -> new RuntimeException("Customer not found"));

        CarLicensePlate carLicensePlate = new CarLicensePlate(request.carLicensePlate());

        vehicleRepository.findByCarLicensePlate(carLicensePlate).ifPresent(vehicle -> {
            throw new DomainException("Vehicle already registered");
        });

        Vehicle vehicle = Vehicle.createVehicle(
                request.customerId(),
                carLicensePlate,
                request.model(),
                request.manufacturer(),
                new Kilometers(request.Kilometers()),
                request.year()
        );

        vehicleRepository.save(vehicle);

        return new AddVehicleResponse(
                vehicle.getVehicleId(),
                carLicensePlate.plate(),
                vehicle.getCustomerId() );

    }
}
