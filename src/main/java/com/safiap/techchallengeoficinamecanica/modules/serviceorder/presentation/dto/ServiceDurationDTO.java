package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.dto;

import java.util.UUID;

public record ServiceDurationDTO(UUID serviceId, long durationSeconds) {
}
