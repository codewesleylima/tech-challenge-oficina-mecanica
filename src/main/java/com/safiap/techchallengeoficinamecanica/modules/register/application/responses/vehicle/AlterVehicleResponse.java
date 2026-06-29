package com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle;

import java.time.Year;
import java.util.UUID;

public record AlterVehicleResponse(
        UUID vehicleId,
        UUID customerId,
        String carLicensePlate,
        String model,
        String manufacturer,
        Integer kilometers,
        Year year
) {
}
