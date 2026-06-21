package com.safiap.techchallengeoficinamecanica.modules.register.application.service;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleResponseDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(CustomerRepository customerRepository, VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public AddVehicleResponseDTO add(AddVehicleDTO request) {
        customerRepository.findByCustomerId(request.getCustomerIdDTO())
                .orElseThrow(() -> new DomainException("Customer not found"));

        CarLicensePlate plate = new CarLicensePlate(request.getCarLicensePlateDTO());

        vehicleRepository.findByCarLicensePlate(plate).ifPresent(existing -> {
            throw new DomainException("Vehicle already registered with this license plate");
        });

        Vehicle vehicle = Vehicle.createVehicle(
                request.getCustomerIdDTO(),
                plate,
                request.getModelDTO(),
                request.getManufacturerDTO(),
                new Kilometers(request.getKilometersDTO()),
                Year.of(request.getYearDTO())
        );

        vehicleRepository.save(vehicle);

        return new AddVehicleResponseDTO(
                vehicle.getVehicleId(),
                vehicle.getCarLicensePlate().plate(),
                vehicle.getCustomerId()
        );
    }
}
