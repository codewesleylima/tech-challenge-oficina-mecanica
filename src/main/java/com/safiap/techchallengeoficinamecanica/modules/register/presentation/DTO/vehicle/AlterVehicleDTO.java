package com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.vehicle;

import java.time.Year;
import java.util.UUID;

public record AlterVehicleDTO(
        UUID customerId,
        String carLicensePlate,
        String model,
        String manufactures,
        Integer kilometers,
        Year year
) {
}
