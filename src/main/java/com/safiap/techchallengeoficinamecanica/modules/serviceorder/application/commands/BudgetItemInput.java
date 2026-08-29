package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;

import java.util.UUID;

public record BudgetItemInput(
        BudgetItemType type,
        UUID itemId,
        String description,
        int quantity
) {}
