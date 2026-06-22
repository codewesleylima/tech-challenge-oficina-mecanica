package com.safiap.techchallengeoficinamecanica.modules.register.application.commands;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;

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
