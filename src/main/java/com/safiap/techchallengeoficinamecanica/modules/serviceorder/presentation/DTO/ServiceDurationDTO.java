package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO;

import java.util.UUID;

public record ServiceDurationDTO(UUID serviceId, long durationSeconds) {
}
