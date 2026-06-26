package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.UUID;

public record OpenServiceOrderCommand(
        UUID customerId,
        UUID vehicleId,
        String problemDescription
) {
}
