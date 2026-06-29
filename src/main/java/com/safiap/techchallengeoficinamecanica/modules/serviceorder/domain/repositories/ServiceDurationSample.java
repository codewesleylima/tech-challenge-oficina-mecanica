package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories;

import java.util.UUID;

public record ServiceDurationSample(UUID serviceId, long durationSeconds) {
}
