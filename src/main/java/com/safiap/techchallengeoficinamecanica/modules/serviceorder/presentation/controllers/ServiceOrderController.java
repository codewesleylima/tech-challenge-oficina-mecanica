package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/service-orders")
public class ServiceOrderController {

    private final OpenServiceOrderUseCase openServiceOrderUseCase;
    private final CreateBudgetUseCase createBudgetUseCase;
    private final AddBudgetPartItemUseCase addBudgetPartItemUseCase;
    private final AddBudgetServiceItemUseCase addBudgetServiceItemUseCase;
    private final ApproveBudgetUseCase approveBudgetUseCase;
    private final RejectBudgetUseCase rejectBudgetUseCase;
    private final StartDiagnosisUseCase startDiagnosisUseCase;
    private final ConcludeDiagnosisUseCase concludeDiagnosisUseCase;
    private final RegisterAuthorizationUseCase registerAuthorizationUseCase;
    private final StartExecutionUseCase startExecutionUseCase;
    private final RegisterExecutedServiceUseCase registerExecutedServiceUseCase;
    private final ConcludeServiceOrderUseCase concludeServiceOrderUseCase;
    private final CancelServiceOrderUseCase cancelServiceOrderUseCase;

    public ServiceOrderController(OpenServiceOrderUseCase openServiceOrderUseCase,
                                   CreateBudgetUseCase createBudgetUseCase,
                                   AddBudgetPartItemUseCase addBudgetPartItemUseCase,
                                   AddBudgetServiceItemUseCase addBudgetServiceItemUseCase,
                                   ApproveBudgetUseCase approveBudgetUseCase,
                                   RejectBudgetUseCase rejectBudgetUseCase,
                                   StartDiagnosisUseCase startDiagnosisUseCase,
                                   ConcludeDiagnosisUseCase concludeDiagnosisUseCase,
                                   RegisterAuthorizationUseCase registerAuthorizationUseCase,
                                   StartExecutionUseCase startExecutionUseCase,
                                   RegisterExecutedServiceUseCase registerExecutedServiceUseCase,
                                   ConcludeServiceOrderUseCase concludeServiceOrderUseCase,
                                   CancelServiceOrderUseCase cancelServiceOrderUseCase) {
        this.openServiceOrderUseCase = openServiceOrderUseCase;
        this.createBudgetUseCase = createBudgetUseCase;
        this.addBudgetPartItemUseCase = addBudgetPartItemUseCase;
        this.addBudgetServiceItemUseCase = addBudgetServiceItemUseCase;
        this.approveBudgetUseCase = approveBudgetUseCase;
        this.rejectBudgetUseCase = rejectBudgetUseCase;
        this.startDiagnosisUseCase = startDiagnosisUseCase;
        this.concludeDiagnosisUseCase = concludeDiagnosisUseCase;
        this.registerAuthorizationUseCase = registerAuthorizationUseCase;
        this.startExecutionUseCase = startExecutionUseCase;
        this.registerExecutedServiceUseCase = registerExecutedServiceUseCase;
        this.concludeServiceOrderUseCase = concludeServiceOrderUseCase;
        this.cancelServiceOrderUseCase = cancelServiceOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> open(@RequestBody OpenServiceOrderDTO request) {
        OpenServiceOrderCommand command = new OpenServiceOrderCommand(request.customerId(), request.vehicleId(), request.problemDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(openServiceOrderUseCase.execute(command));
    }

    @PostMapping("/{id}/budget")
    public ResponseEntity<ServiceOrderResponse> createBudget(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBudgetUseCase.execute(new CreateBudgetCommand(id)));
    }

    @PostMapping("/{id}/budget/items/parts")
    public ResponseEntity<ServiceOrderResponse> addPartItem(@PathVariable UUID id, @RequestBody AddBudgetPartItemDTO request) {
        AddBudgetPartItemCommand command = new AddBudgetPartItemCommand(id, request.partId(), request.description(), request.quantity(), request.unitPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body(addBudgetPartItemUseCase.execute(command));
    }

    @PostMapping("/{id}/budget/items/services")
    public ResponseEntity<ServiceOrderResponse> addServiceItem(@PathVariable UUID id, @RequestBody AddBudgetServiceItemDTO request) {
        AddBudgetServiceItemCommand command = new AddBudgetServiceItemCommand(id, request.description(), request.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(addBudgetServiceItemUseCase.execute(command));
    }

    @PatchMapping("/{id}/budget/approve")
    public ResponseEntity<ServiceOrderResponse> approveBudget(@PathVariable UUID id) {
        return ResponseEntity.ok(approveBudgetUseCase.execute(new ApproveBudgetCommand(id)));
    }

    @PatchMapping("/{id}/budget/reject")
    public ResponseEntity<ServiceOrderResponse> rejectBudget(@PathVariable UUID id) {
        return ResponseEntity.ok(rejectBudgetUseCase.execute(new RejectBudgetCommand(id)));
    }

    @PatchMapping("/{id}/diagnosis/start")
    public ResponseEntity<ServiceOrderResponse> startDiagnosis(@PathVariable UUID id) {
        return ResponseEntity.ok(startDiagnosisUseCase.execute(new StartDiagnosisCommand(id)));
    }

    @PatchMapping("/{id}/diagnosis/conclude")
    public ResponseEntity<ServiceOrderResponse> concludeDiagnosis(@PathVariable UUID id, @RequestBody ConcludeDiagnosisDTO request) {
        ConcludeDiagnosisCommand command = new ConcludeDiagnosisCommand(id, request.notes());
        return ResponseEntity.ok(concludeDiagnosisUseCase.execute(command));
    }

    @PostMapping("/{id}/authorization")
    public ResponseEntity<ServiceOrderResponse> registerAuthorization(@PathVariable UUID id, @RequestBody RegisterAuthorizationDTO request) {
        RegisterAuthorizationCommand command = new RegisterAuthorizationCommand(id, request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerAuthorizationUseCase.execute(command));
    }

    @PatchMapping("/{id}/execution/start")
    public ResponseEntity<ServiceOrderResponse> startExecution(@PathVariable UUID id) {
        return ResponseEntity.ok(startExecutionUseCase.execute(new StartExecutionCommand(id)));
    }

    @PostMapping("/{id}/execution/services")
    public ResponseEntity<ServiceOrderResponse> registerExecutedService(@PathVariable UUID id, @RequestBody RegisterExecutedServiceDTO request) {
        RegisterExecutedServiceCommand command = new RegisterExecutedServiceCommand(id, request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerExecutedServiceUseCase.execute(command));
    }

    @PatchMapping("/{id}/conclude")
    public ResponseEntity<ServiceOrderResponse> conclude(@PathVariable UUID id) {
        return ResponseEntity.ok(concludeServiceOrderUseCase.execute(new ConcludeServiceOrderCommand(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ServiceOrderResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(cancelServiceOrderUseCase.execute(new CancelServiceOrderCommand(id)));
    }
}
