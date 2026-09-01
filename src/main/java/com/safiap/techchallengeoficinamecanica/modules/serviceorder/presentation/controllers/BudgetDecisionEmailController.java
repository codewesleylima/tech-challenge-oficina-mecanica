package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.ApproveBudgetCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.ApproveBudgetUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.GetBudgetUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.GetServiceOrderByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.RejectBudgetUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.pages.BudgetDecisionPage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@RestController
@RequestMapping("/service-orders/{serviceOrderId}/budget")
@PreAuthorize("permitAll()")
public class BudgetDecisionEmailController {

    private static final String HTML_UTF8 = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8";

    private final ApproveBudgetUseCase approveBudgetUseCase;
    private final RejectBudgetUseCase rejectBudgetUseCase;
    private final GetServiceOrderByIdUseCase getServiceOrderByIdUseCase;
    private final GetBudgetUseCase getBudgetUseCase;

    public BudgetDecisionEmailController(ApproveBudgetUseCase approveBudgetUseCase,
                                         RejectBudgetUseCase rejectBudgetUseCase,
                                         GetServiceOrderByIdUseCase getServiceOrderByIdUseCase,
                                         GetBudgetUseCase getBudgetUseCase) {
        this.approveBudgetUseCase = approveBudgetUseCase;
        this.rejectBudgetUseCase = rejectBudgetUseCase;
        this.getServiceOrderByIdUseCase = getServiceOrderByIdUseCase;
        this.getBudgetUseCase = getBudgetUseCase;
    }

    @GetMapping(value = "/approve", produces = HTML_UTF8)
    public ResponseEntity<String> approve(@PathVariable UUID serviceOrderId) {
        BudgetResponse budget = approveBudgetUseCase.execute(new ApproveBudgetCommand(serviceOrderId));
        ServiceOrderResponse serviceOrder = getServiceOrderByIdUseCase.execute(serviceOrderId);

        return html(BudgetDecisionPage.approved(serviceOrder, budget));
    }

    @GetMapping(value = "/reject", produces = HTML_UTF8)
    public ResponseEntity<String> reject(@PathVariable UUID serviceOrderId) {
        ServiceOrderResponse serviceOrder = rejectBudgetUseCase.execute(serviceOrderId);
        BudgetResponse budget = getBudgetUseCase.execute(serviceOrderId);

        return html(BudgetDecisionPage.rejected(serviceOrder, budget));
    }

    private ResponseEntity<String> html(String body) {
        return ResponseEntity.ok()
                .contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8))
                .body(body);
    }
}
