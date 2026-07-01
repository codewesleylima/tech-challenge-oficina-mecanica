package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.AddPartDTO;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.AddServiceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/service-orders/{serviceOrderId}/budget")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class BudgetController {

    private final OpenBudgetUseCase openBudgetUseCase;
    private final AddPartToBudgetUseCase addPartToBudgetUseCase;
    private final AddServiceToBudgetUseCase addServiceToBudgetUseCase;
    private final GetBudgetUseCase getBudgetUseCase;
    private final FinalizeBudgetUseCase finalizeBudgetUseCase;
    private final CompleteServiceItemUseCase completeServiceItemUseCase;

    public BudgetController(OpenBudgetUseCase openBudgetUseCase,
                            AddPartToBudgetUseCase addPartToBudgetUseCase,
                            AddServiceToBudgetUseCase addServiceToBudgetUseCase,
                            GetBudgetUseCase getBudgetUseCase,
                            FinalizeBudgetUseCase finalizeBudgetUseCase,
                            CompleteServiceItemUseCase completeServiceItemUseCase) {
        this.openBudgetUseCase = openBudgetUseCase;
        this.addPartToBudgetUseCase = addPartToBudgetUseCase;
        this.addServiceToBudgetUseCase = addServiceToBudgetUseCase;
        this.getBudgetUseCase = getBudgetUseCase;
        this.finalizeBudgetUseCase = finalizeBudgetUseCase;
        this.completeServiceItemUseCase = completeServiceItemUseCase;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> openBudget(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(openBudgetUseCase.execute(serviceOrderId));
    }

    @GetMapping
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(getBudgetUseCase.execute(serviceOrderId));
    }

    @PostMapping("/parts")
    public ResponseEntity<BudgetResponse> addPart(
            @PathVariable UUID serviceOrderId, @RequestBody AddPartDTO request) {
        AddPartCommand command = new AddPartCommand(
                serviceOrderId, request.itemId(), request.description(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(addPartToBudgetUseCase.execute(command));
    }

    @PostMapping("/services")
    public ResponseEntity<BudgetResponse> addService(
            @PathVariable UUID serviceOrderId, @RequestBody AddServiceDTO request) {
        AddServiceCommand command = new AddServiceCommand(
                serviceOrderId, request.itemId(), request.description(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(addServiceToBudgetUseCase.execute(command));
    }

    @PatchMapping("/finalize")
    public ResponseEntity<BudgetResponse> finalizeBudget(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(finalizeBudgetUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/items/{budgetItemId}/complete")
    public ResponseEntity<BudgetResponse> completeServiceItem(
            @PathVariable UUID serviceOrderId, @PathVariable UUID budgetItemId) {
        return ResponseEntity.ok(completeServiceItemUseCase.execute(serviceOrderId, budgetItemId));
    }
}
