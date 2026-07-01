package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.AverageServiceTimeResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.GetAverageServiceTimeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service-orders/{serviceOrderId}/metrics")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class MetricsController {

    private final GetAverageServiceTimeUseCase getAverageServiceTimeUseCase;

    public MetricsController(GetAverageServiceTimeUseCase getAverageServiceTimeUseCase) {
        this.getAverageServiceTimeUseCase = getAverageServiceTimeUseCase;
    }

    @GetMapping("/average-execution-time")
    public ResponseEntity<List<AverageServiceTimeResponse>> averageExecutionTime(
            @PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(getAverageServiceTimeUseCase.execute(serviceOrderId));
    }
}
