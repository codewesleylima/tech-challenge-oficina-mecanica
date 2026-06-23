package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.UUID;

public record RegisterAuthorizationCommand(UUID serviceOrderId, String notes) {}
