package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.GetPartByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases.GetServiceByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryCatalogAdapter implements InventoryCatalogPort {

    private final GetPartByIdUseCase getPartByIdUseCase;
    private final GetServiceByIdUseCase getServiceByIdUseCase;

    public InventoryCatalogAdapter(GetPartByIdUseCase getPartByIdUseCase,
                                   GetServiceByIdUseCase getServiceByIdUseCase) {
        this.getPartByIdUseCase = getPartByIdUseCase;
        this.getServiceByIdUseCase = getServiceByIdUseCase;
    }
    @Override
    public void ensurePartExists(UUID partId) {
        getPartByIdUseCase.execute(partId);
    }
    @Override
    public void ensureServiceExists(UUID serviceId) {
        getServiceByIdUseCase.execute(serviceId);
    }
}
