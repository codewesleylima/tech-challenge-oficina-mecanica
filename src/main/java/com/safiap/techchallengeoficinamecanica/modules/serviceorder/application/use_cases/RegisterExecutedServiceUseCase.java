package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.RegisterExecutedServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterExecutedServiceUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public RegisterExecutedServiceUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(RegisterExecutedServiceCommand command) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(command.serviceOrderId())
                .orElseThrow(() -> new DomainException("Service order not found"));
        serviceOrder.registerExecutedService(command.description());
        serviceOrderRepository.save(serviceOrder);
        return toResponse(serviceOrder);
    }

    private ServiceOrderResponse toResponse(ServiceOrder so) {
        return new ServiceOrderResponse(so.getServiceOrderId(), so.getCustomerId(), so.getVehicleId(),
                so.getProblemDescription(), so.getStatus().name(), so.getOpenedAt(), so.getConcludedAt());
    }
}
