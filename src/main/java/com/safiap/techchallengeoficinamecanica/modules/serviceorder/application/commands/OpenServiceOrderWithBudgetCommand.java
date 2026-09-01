package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.List;
import java.util.UUID;

public record OpenServiceOrderWithBudgetCommand(
        UUID customerId,
        UUID vehicleId,
        String problemDescription,
        List<BudgetItemInput> items
) {}