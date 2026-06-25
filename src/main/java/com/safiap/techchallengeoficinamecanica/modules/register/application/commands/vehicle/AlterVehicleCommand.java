package com.safiap.techchallengeoficinamecanica.modules.register.application.commands.vehicle;

import java.time.Year;
import java.util.UUID;

public record AlterVehicleCommand(
        UUID vehicleId,
        UUID customerId,
        String carLicensePlate,
        String model,
        String manufactures,
        Integer kilometers,
        Year year
) {
}
