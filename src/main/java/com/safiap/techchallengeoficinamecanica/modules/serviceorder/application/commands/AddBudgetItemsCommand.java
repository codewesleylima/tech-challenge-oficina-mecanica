package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.List;
import java.util.UUID;

public record AddBudgetItemsCommand(
        UUID serviceOrderId,
        List<BudgetItemInput> items
) {}
