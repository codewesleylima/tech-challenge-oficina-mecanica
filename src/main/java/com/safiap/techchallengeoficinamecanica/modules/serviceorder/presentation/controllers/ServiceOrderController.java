package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.RegistraTempoServicoCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceTimeRecordResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.OpenServiceOrderDTO;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.RegistraTempoServicoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service-orders")
public class ServiceOrderController {

    private final OpenServiceOrderUseCase openServiceOrderUseCase;
    private final GetServiceOrderByIdUseCase getServiceOrderByIdUseCase;
    private final ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase;
    private final PullServiceOrderUseCase pullServiceOrderUseCase;
    private final IncreaseServiceOrderPriorityUseCase increaseServiceOrderPriorityUseCase;
    private final DecreaseServiceOrderPriorityUseCase decreaseServiceOrderPriorityUseCase;
    private final IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase;
    private final FinalizarDiagnosticoUseCase finalizarDiagnosticoUseCase;
    private final ExecutarOrdemServicoUseCase executarOrdemServicoUseCase;
    private final RejeitarOrcamentoUseCase rejeitarOrcamentoUseCase;
    private final RegistraTempoServicoUseCase registraTempoServicoUseCase;
    private final FinalizarOsUseCase finalizarOsUseCase;
    private final EntregarOrdemServicoUseCase entregarOrdemServicoUseCase;
    private final ListaOsPorStatusUseCase listaOsPorStatusUseCase;

    public ServiceOrderController(OpenServiceOrderUseCase openServiceOrderUseCase,
                                  GetServiceOrderByIdUseCase getServiceOrderByIdUseCase,
                                  ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase,
                                  PullServiceOrderUseCase pullServiceOrderUseCase,
                                  IncreaseServiceOrderPriorityUseCase increaseServiceOrderPriorityUseCase,
                                  DecreaseServiceOrderPriorityUseCase decreaseServiceOrderPriorityUseCase,
                                  IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase,
                                  FinalizarDiagnosticoUseCase finalizarDiagnosticoUseCase,
                                  ExecutarOrdemServicoUseCase executarOrdemServicoUseCase,
                                  RejeitarOrcamentoUseCase rejeitarOrcamentoUseCase,
                                  RegistraTempoServicoUseCase registraTempoServicoUseCase,
                                  FinalizarOsUseCase finalizarOsUseCase,
                                  EntregarOrdemServicoUseCase entregarOrdemServicoUseCase,
                                  ListaOsPorStatusUseCase listaOsPorStatusUseCase) {
        this.openServiceOrderUseCase = openServiceOrderUseCase;
        this.getServiceOrderByIdUseCase = getServiceOrderByIdUseCase;
        this.listServiceOrdersByCustomerUseCase = listServiceOrdersByCustomerUseCase;
        this.pullServiceOrderUseCase = pullServiceOrderUseCase;
        this.increaseServiceOrderPriorityUseCase = increaseServiceOrderPriorityUseCase;
        this.decreaseServiceOrderPriorityUseCase = decreaseServiceOrderPriorityUseCase;
        this.iniciarDiagnosticoUseCase = iniciarDiagnosticoUseCase;
        this.finalizarDiagnosticoUseCase = finalizarDiagnosticoUseCase;
        this.executarOrdemServicoUseCase = executarOrdemServicoUseCase;
        this.rejeitarOrcamentoUseCase = rejeitarOrcamentoUseCase;
        this.registraTempoServicoUseCase = registraTempoServicoUseCase;
        this.finalizarOsUseCase = finalizarOsUseCase;
        this.entregarOrdemServicoUseCase = entregarOrdemServicoUseCase;
        this.listaOsPorStatusUseCase = listaOsPorStatusUseCase;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> openServiceOrder(@RequestBody OpenServiceOrderDTO request) {
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
    public ResponseEntity<List<ServiceOrderResponse>> listByStatus(@RequestParam ServiceOrderStatus status) {
        return ResponseEntity.ok(listaOsPorStatusUseCase.execute(status));
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
        return ResponseEntity.ok(iniciarDiagnosticoUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/finalize-diagnosis")
    public ResponseEntity<ServiceOrderResponse> finalizeDiagnosis(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(finalizarDiagnosticoUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/execute")
    public ResponseEntity<ServiceOrderResponse> executeOrder(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(executarOrdemServicoUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/reject-budget")
    public ResponseEntity<ServiceOrderResponse> rejectBudget(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(rejeitarOrcamentoUseCase.execute(serviceOrderId));
    }

    @PostMapping("/{serviceOrderId}/time-records")
    public ResponseEntity<ServiceTimeRecordResponse> registraTempoServico(
            @PathVariable UUID serviceOrderId, @RequestBody RegistraTempoServicoDTO request) {
        RegistraTempoServicoCommand command = new RegistraTempoServicoCommand(
                serviceOrderId, request.startTime(), request.endTime(), request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(registraTempoServicoUseCase.execute(command));
    }

    @PatchMapping("/{serviceOrderId}/finalize")
    public ResponseEntity<ServiceOrderResponse> finalizeOrder(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(finalizarOsUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/deliver")
    public ResponseEntity<ServiceOrderResponse> deliver(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(entregarOrdemServicoUseCase.execute(serviceOrderId));
    }
}
