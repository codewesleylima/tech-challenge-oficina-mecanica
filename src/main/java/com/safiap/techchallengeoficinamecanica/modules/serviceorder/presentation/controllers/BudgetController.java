package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AdicionarPecaInsumoCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AdicionarServicoCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.AdicionarPecaInsumoDTO;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.AdicionarServicoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/service-orders/{serviceOrderId}/budget")
public class BudgetController {

    private final IniciarOrcamentoUseCase iniciarOrcamentoUseCase;
    private final AdicionarPecaInsumoOrcamentoUseCase adicionarPecaInsumoOrcamentoUseCase;
    private final AdicionarServicoOrcamentoUseCase adicionarServicoOrcamentoUseCase;
    private final CalcularOrcamentoUseCase calcularOrcamentoUseCase;
    private final FinalizarOrcamentoUseCase finalizarOrcamentoUseCase;

    public BudgetController(IniciarOrcamentoUseCase iniciarOrcamentoUseCase,
                            AdicionarPecaInsumoOrcamentoUseCase adicionarPecaInsumoOrcamentoUseCase,
                            AdicionarServicoOrcamentoUseCase adicionarServicoOrcamentoUseCase,
                            CalcularOrcamentoUseCase calcularOrcamentoUseCase,
                            FinalizarOrcamentoUseCase finalizarOrcamentoUseCase) {
        this.iniciarOrcamentoUseCase = iniciarOrcamentoUseCase;
        this.adicionarPecaInsumoOrcamentoUseCase = adicionarPecaInsumoOrcamentoUseCase;
        this.adicionarServicoOrcamentoUseCase = adicionarServicoOrcamentoUseCase;
        this.calcularOrcamentoUseCase = calcularOrcamentoUseCase;
        this.finalizarOrcamentoUseCase = finalizarOrcamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> iniciarOrcamento(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iniciarOrcamentoUseCase.execute(serviceOrderId));
    }

    @GetMapping
    public ResponseEntity<BudgetResponse> calcularOrcamento(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(calcularOrcamentoUseCase.execute(serviceOrderId));
    }

    @PostMapping("/parts")
    public ResponseEntity<BudgetResponse> adicionarPeca(
            @PathVariable UUID serviceOrderId, @RequestBody AdicionarPecaInsumoDTO request) {
        AdicionarPecaInsumoCommand command = new AdicionarPecaInsumoCommand(
                serviceOrderId, request.itemId(), request.description(), request.quantity(), request.unitPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarPecaInsumoOrcamentoUseCase.execute(command));
    }

    @PostMapping("/services")
    public ResponseEntity<BudgetResponse> adicionarServico(
            @PathVariable UUID serviceOrderId, @RequestBody AdicionarServicoDTO request) {
        AdicionarServicoCommand command = new AdicionarServicoCommand(
                serviceOrderId, request.itemId(), request.description(), request.quantity(), request.unitPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarServicoOrcamentoUseCase.execute(command));
    }

    @PatchMapping("/finalize")
    public ResponseEntity<BudgetResponse> finalizarOrcamento(@PathVariable UUID serviceOrderId) {
        return ResponseEntity.ok(finalizarOrcamentoUseCase.execute(serviceOrderId));
    }
}
