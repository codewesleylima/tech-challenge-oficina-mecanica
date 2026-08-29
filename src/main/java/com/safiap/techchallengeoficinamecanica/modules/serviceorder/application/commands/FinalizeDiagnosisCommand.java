package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands;

import java.util.List;
import java.util.UUID;

public record FinalizeDiagnosisCommand(
        UUID serviceOrderId,
        String diagnosis,
        List<BudgetItemInput> items
) {}
