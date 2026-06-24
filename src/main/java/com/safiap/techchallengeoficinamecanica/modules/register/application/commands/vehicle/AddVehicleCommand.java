package com.safiap.techchallengeoficinamecanica.modules.register.application.commands.vehicle;

import java.time.Year;
import java.util.UUID;

public record AddVehicleCommand(
        UUID customerId,
        String carLicensePlate,
        String model,
        String manufacturer,
        Integer Kilometers,
        Year year
) {
}
