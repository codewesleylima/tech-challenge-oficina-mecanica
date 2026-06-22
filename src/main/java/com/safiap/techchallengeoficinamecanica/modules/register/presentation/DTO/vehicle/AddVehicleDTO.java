package com.safiap.techchallengeoficinamecanica.modules.register.presentation.DTO.vehicle;

import java.time.Year;
import java.util.UUID;

public record AddVehicleDTO (
            UUID customerId,
            String carLicensePlate,
            String model,
            String manufacturer,
            Integer kilometers,
            Year year
){
}
