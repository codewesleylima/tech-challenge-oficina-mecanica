package com.safiap.techchallengeoficinamecanica.modules.register.presentation.controller;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerResponseDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<RegisterCustomerResponseDTO> register(
            @RequestBody RegisterCustomerDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.register(request));
    }
}
