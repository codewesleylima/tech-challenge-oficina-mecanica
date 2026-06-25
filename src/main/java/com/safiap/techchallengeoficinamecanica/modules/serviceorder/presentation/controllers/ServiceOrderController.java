package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.DecreaseServiceOrderPriorityUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.GetServiceOrderByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.IncreaseServiceOrderPriorityUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.ListServiceOrdersByCustomerUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.OpenServiceOrderUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.PullServiceOrderUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.OpenServiceOrderDTO;
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

    public ServiceOrderController(OpenServiceOrderUseCase openServiceOrderUseCase,
                                  GetServiceOrderByIdUseCase getServiceOrderByIdUseCase,
                                  ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase,
                                  PullServiceOrderUseCase pullServiceOrderUseCase,
                                  IncreaseServiceOrderPriorityUseCase increaseServiceOrderPriorityUseCase,
                                  DecreaseServiceOrderPriorityUseCase decreaseServiceOrderPriorityUseCase) {
        this.openServiceOrderUseCase = openServiceOrderUseCase;
        this.getServiceOrderByIdUseCase = getServiceOrderByIdUseCase;
        this.listServiceOrdersByCustomerUseCase = listServiceOrdersByCustomerUseCase;
        this.pullServiceOrderUseCase = pullServiceOrderUseCase;
        this.increaseServiceOrderPriorityUseCase = increaseServiceOrderPriorityUseCase;
        this.decreaseServiceOrderPriorityUseCase = decreaseServiceOrderPriorityUseCase;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> openServiceOrder(@RequestBody OpenServiceOrderDTO request) {
        OpenServiceOrderCommand command = new OpenServiceOrderCommand(
                request.customerId(),
                request.vehicleId(),
                request.problemDescription()
        );
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


    @PatchMapping("/{serviceOrderId}/priority/increase")
    public ResponseEntity<ServiceOrderResponse> increasePriority(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(increaseServiceOrderPriorityUseCase.execute(serviceOrderId));
    }

    @PatchMapping("/{serviceOrderId}/priority/decrease")
    public ResponseEntity<ServiceOrderResponse> decreasePriority(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(decreaseServiceOrderPriorityUseCase.execute(serviceOrderId));
    }
}
