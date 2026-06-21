package com.safiap.techchallengeoficinamecanica.modules.register.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.AddVehicleCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.AddVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.AddVehicleUseCase;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.vehicle.AddVehicleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final AddVehicleUseCase addVehicleUseCase;

    public VehicleController(AddVehicleUseCase addVehicleUseCase) {
        this.addVehicleUseCase = addVehicleUseCase;
    }

    @PostMapping
    public ResponseEntity<AddVehicleResponse> addVehicle(
            @RequestBody AddVehicleDTO request) {

        AddVehicleCommand command = new AddVehicleCommand(
                request.customerId(),
                request.carLicensePlate(),
                request.model(),
                request.manufacturer(),
                request.kilometers(),
                request.year()
        );

        AddVehicleResponse response = addVehicleUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    };

}
