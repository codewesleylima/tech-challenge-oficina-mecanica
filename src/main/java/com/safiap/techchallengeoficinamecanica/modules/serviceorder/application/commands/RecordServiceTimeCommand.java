package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecordServiceTimeCommand(
        UUID serviceOrderId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String notes
) {}
