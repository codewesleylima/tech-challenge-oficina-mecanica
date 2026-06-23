package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO;

import java.util.UUID;

public record OpenServiceOrderDTO(UUID customerId, UUID vehicleId, String problemDescription) {}
