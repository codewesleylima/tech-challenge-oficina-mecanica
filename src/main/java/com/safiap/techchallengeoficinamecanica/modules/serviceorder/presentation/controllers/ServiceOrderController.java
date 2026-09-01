package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.FinalizeDiagnosisCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderWithBudgetCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderStatusResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderWithBudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.BudgetItemMapper;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.FinalizeDiagnosisDTO;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.OpenServiceOrderDTO;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.OpenServiceOrderWithBudgetDTO;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service-orders")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ServiceOrderController {

    private static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    private final OpenServiceOrderUseCase openServiceOrderUseCase;
    private final GetServiceOrderByIdUseCase getServiceOrderByIdUseCase;
    private final ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase;
    private final ListServiceOrdersByStatusUseCase listServiceOrdersByStatusUseCase;
    private final PullServiceOrderUseCase pullServiceOrderUseCase;
    private final IncreaseServiceOrderPriorityUseCase increaseServiceOrderPriorityUseCase;
    private final DecreaseServiceOrderPriorityUseCase decreaseServiceOrderPriorityUseCase;
    private final StartDiagnosisUseCase startDiagnosisUseCase;
    private final FinalizeDiagnosisUseCase finalizeDiagnosisUseCase;
    private final StartServiceOrderExecutionUseCase startServiceOrderExecutionUseCase;
    private final RejectBudgetUseCase rejectBudgetUseCase;
    private final FinalizeServiceOrderUseCase finalizeServiceOrderUseCase;
    private final DeliverServiceOrderUseCase deliverServiceOrderUseCase;
    private final GetServiceOrderStatusUseCase getServiceOrderStatusUseCase;
    private final GetAllServiceOrdersUseCase getAllServiceOrdersUseCase;;
    private final OpenServiceOrderWithBudgetUseCase openServiceOrderWithBudgetUseCase;

    public ServiceOrderController(OpenServiceOrderUseCase openServiceOrderUseCase,
                                  GetServiceOrderByIdUseCase getServiceOrderByIdUseCase,
                                  ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase,
                                  ListServiceOrdersByStatusUseCase listServiceOrdersByStatusUseCase,
                                  PullServiceOrderUseCase pullServiceOrderUseCase,
                                  IncreaseServiceOrderPriorityUseCase increaseServiceOrderPriorityUseCase,
                                  DecreaseServiceOrderPriorityUseCase decreaseServiceOrderPriorityUseCase,
                                  StartDiagnosisUseCase startDiagnosisUseCase,
                                  FinalizeDiagnosisUseCase finalizeDiagnosisUseCase,
                                  StartServiceOrderExecutionUseCase startServiceOrderExecutionUseCase,
                                  RejectBudgetUseCase rejectBudgetUseCase,
                                  FinalizeServiceOrderUseCase finalizeServiceOrderUseCase,
                                  DeliverServiceOrderUseCase deliverServiceOrderUseCase,
                                  GetServiceOrderStatusUseCase getServiceOrderStatusUseCase,
                                  GetAllServiceOrdersUseCase getAllServiceOrdersUseCase,
                                  OpenServiceOrderWithBudgetUseCase openServiceOrderWithBudgetUseCase) {
        this.openServiceOrderUseCase = openServiceOrderUseCase;
        this.getServiceOrderByIdUseCase = getServiceOrderByIdUseCase;
        this.listServiceOrdersByCustomerUseCase = listServiceOrdersByCustomerUseCase;
        this.listServiceOrdersByStatusUseCase = listServiceOrdersByStatusUseCase;
        this.pullServiceOrderUseCase = pullServiceOrderUseCase;
        this.increaseServiceOrderPriorityUseCase = increaseServiceOrderPriorityUseCase;
        this.decreaseServiceOrderPriorityUseCase = decreaseServiceOrderPriorityUseCase;
        this.startDiagnosisUseCase = startDiagnosisUseCase;
        this.finalizeDiagnosisUseCase = finalizeDiagnosisUseCase;
        this.startServiceOrderExecutionUseCase = startServiceOrderExecutionUseCase;
        this.rejectBudgetUseCase = rejectBudgetUseCase;
        this.finalizeServiceOrderUseCase = finalizeServiceOrderUseCase;
        this.deliverServiceOrderUseCase = deliverServiceOrderUseCase;
        this.getServiceOrderStatusUseCase = getServiceOrderStatusUseCase;
        this.getAllServiceOrdersUseCase = getAllServiceOrdersUseCase;
        this.openServiceOrderWithBudgetUseCase = openServiceOrderWithBudgetUseCase;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> openServiceOrder(@Valid @RequestBody OpenServiceOrderDTO request) {
        OpenServiceOrderCommand command = new OpenServiceOrderCommand(
                request.customerId(), request.vehicleId(), request.problemDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(openServiceOrderUseCase.execute(command));
    }

    @GetMapping("/{serviceOrderId}")
    public ResponseEntity<ServiceOrderResponse> getById(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(getServiceOrderByIdUseCase.execute(serviceOrderId));
    }

    @GetMapping("/pullNext")
    public ResponseEntity<ServiceOrderResponse> pullNext() {
        return ResponseEntity.ok(pullServiceOrderUseCase.execute());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ServiceOrderResponse>> listByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(listServiceOrdersByCustomerUseCase.execute(customerId));
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrderResponse>> listAllByStatus(@RequestParam ServiceOrderStatus status) {
        return ResponseEntity.ok(listServiceOrdersByStatusUseCase.execute(status));
    }

    @GetMapping("/status/{serviceOrderId}")
    public ResponseEntity<ServiceOrderStatusResponse> getServiceOrderStatus(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(getServiceOrderStatusUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/priority/increase")
    public ResponseEntity<ServiceOrderResponse> increasePriority(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(increaseServiceOrderPriorityUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/priority/decrease")
    public ResponseEntity<ServiceOrderResponse> decreasePriority(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(decreaseServiceOrderPriorityUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/start-diagnosis")
    public ResponseEntity<ServiceOrderResponse> startDiagnosis(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(startDiagnosisUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/finalize-diagnosis")
    public ResponseEntity<ServiceOrderResponse> finalizeDiagnosis(
            @PathVariable UUID serviceOrderId, @Valid @RequestBody FinalizeDiagnosisDTO request) {
        FinalizeDiagnosisCommand command = new FinalizeDiagnosisCommand(
                serviceOrderId, request.diagnosis(), BudgetItemMapper.toInputs(request.items()));
        return ResponseEntity.ok(finalizeDiagnosisUseCase.execute(command));
    }

    @PatchMapping("/{serviceOrderId}/execute")
    public ResponseEntity<ServiceOrderResponse> executeOrder(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(startServiceOrderExecutionUseCase.execute(serviceOrderId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'CUSTOMER')")
    @PatchMapping("/{serviceOrderId}/reject-budget")
    public ResponseEntity<ServiceOrderResponse> rejectBudget(@PathVariable UUID serviceOrderId,
                                                             Authentication authentication) {
        if (!isCustomer(authentication)) {
            return ResponseEntity.ok(rejectBudgetUseCase.execute(serviceOrderId));
        }

        String customerId = ((Jwt) authentication.getPrincipal()).getClaimAsString("customerId");
        if (customerId == null) {
            throw new AuthException("Authenticated user is not linked to a customer");
        }
        return ResponseEntity.ok(
                rejectBudgetUseCase.executeAsCustomer(serviceOrderId, UUID.fromString(customerId)));
    }

    private boolean isCustomer(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ROLE_CUSTOMER.equals(authority.getAuthority()));
    }

    @PatchMapping("/{serviceOrderId}/finalize")
    public ResponseEntity<ServiceOrderResponse> finalizeOrder(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(finalizeServiceOrderUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/deliver")
    public ResponseEntity<ServiceOrderResponse> deliver(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(deliverServiceOrderUseCase.execute(serviceOrderId));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<ServiceOrderResponse>> getMyServiceOrders(@AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getClaimAsString("customerId");
        if (customerId == null) {
            throw new AuthException("Authenticated user is not linked to a customer");
        }
        return ResponseEntity.ok(listServiceOrdersByCustomerUseCase.execute(UUID.fromString(customerId)));
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<ServiceOrderResponse>> getAllServiceOrders() {
        return ResponseEntity.ok(getAllServiceOrdersUseCase.execute());
    }

    @PostMapping("/with-budget")
    public ResponseEntity<ServiceOrderWithBudgetResponse> openServiceOrderWithBudget(
            @Valid @RequestBody OpenServiceOrderWithBudgetDTO request) {
        OpenServiceOrderWithBudgetCommand command = new OpenServiceOrderWithBudgetCommand(
                request.customerId(), request.vehicleId(), request.problemDescription(),
                BudgetItemMapper.toInputs(request.items()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(openServiceOrderWithBudgetUseCase.execute(command));
    }
}
