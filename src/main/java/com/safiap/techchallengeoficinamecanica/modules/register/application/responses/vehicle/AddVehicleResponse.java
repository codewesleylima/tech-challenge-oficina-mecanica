package com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle;

import java.util.UUID;

public record AddVehicleResponse(UUID vehicleId, String plate, UUID customerId) {
}
