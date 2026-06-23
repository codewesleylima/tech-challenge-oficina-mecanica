package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.UUID;

public record ConcludeDiagnosisCommand(UUID serviceOrderId, String notes) {}
