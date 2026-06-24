package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.GetServiceOrderByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.ListServiceOrdersByCustomerUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service-orders")
public class ServiceOrderController {

    private final GetServiceOrderByIdUseCase getServiceOrderByIdUseCase;
    private final ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase;

    public ServiceOrderController(GetServiceOrderByIdUseCase getServiceOrderByIdUseCase,
                                  ListServiceOrdersByCustomerUseCase listServiceOrdersByCustomerUseCase) {
        this.getServiceOrderByIdUseCase = getServiceOrderByIdUseCase;
        this.listServiceOrdersByCustomerUseCase = listServiceOrdersByCustomerUseCase;
    }

    @GetMapping("/{serviceOrderId}")
    public ResponseEntity<ServiceOrderResponse> getById(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(getServiceOrderByIdUseCase.execute(serviceOrderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ServiceOrderResponse>> listByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(listServiceOrdersByCustomerUseCase.execute(customerId));
    }
}
