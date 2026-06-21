package com.safiap.techchallengeoficinamecanica.modules.register.presentation.controller;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleResponseDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<AddVehicleResponseDTO> add(
            @RequestBody AddVehicleDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.add(request));
    }
}
