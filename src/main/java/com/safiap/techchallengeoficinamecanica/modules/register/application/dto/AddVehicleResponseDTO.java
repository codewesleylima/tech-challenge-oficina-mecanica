package com.safiap.techchallengeoficinamecanica.modules.register.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVehicleResponseDTO {
    private UUID vehicleIdDTO;
    private String carLicensePlateDTO;
    private UUID customerIdDTO;
}
