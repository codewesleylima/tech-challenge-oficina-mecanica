package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.UUID;

public record AddPartCommand(
        UUID serviceOrderId,
        UUID itemId,
        String description,
        int quantity
) {}
