package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetPartItemCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddBudgetPartItemUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public AddBudgetPartItemUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(AddBudgetPartItemCommand command) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(command.serviceOrderId())
                .orElseThrow(() -> new DomainException("Service order not found"));
        serviceOrder.addBudgetPartItem(command.partId(), command.description(), command.quantity(), command.unitPrice());
        serviceOrderRepository.save(serviceOrder);
        return toResponse(serviceOrder);
    }

    private ServiceOrderResponse toResponse(ServiceOrder so) {
        return new ServiceOrderResponse(so.getServiceOrderId(), so.getCustomerId(), so.getVehicleId(),
                so.getProblemDescription(), so.getStatus().name(), so.getOpenedAt(), so.getConcludedAt());
    }
}
