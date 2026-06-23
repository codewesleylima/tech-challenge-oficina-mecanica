package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.UUID;

public record RegisterExecutedServiceCommand(UUID serviceOrderId, String description) {}
