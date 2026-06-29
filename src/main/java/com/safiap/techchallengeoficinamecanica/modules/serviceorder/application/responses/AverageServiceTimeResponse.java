package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import java.util.UUID;

public record AverageServiceTimeResponse(UUID serviceId, double averageMinutes, long sampleCount) {
}
