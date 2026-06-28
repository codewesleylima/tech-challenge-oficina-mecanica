package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.DecreasePartStockUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.PartStockPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryPartStockAdapter implements PartStockPort {

    private final DecreasePartStockUseCase decreasePartStockUseCase;

    public InventoryPartStockAdapter(DecreasePartStockUseCase decreasePartStockUseCase) {
        this.decreasePartStockUseCase = decreasePartStockUseCase;
    }
    @Override
    public void decreaseStock(UUID partId, int quantity) {
        decreasePartStockUseCase.execute(partId, quantity);
    }
}
