package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public OpenServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository,
                                   CustomerRepository customerRepository,
                                   VehicleRepository vehicleRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(OpenServiceOrderCommand command) {
        customerRepository.findByCustomerId(command.customerId())
                .orElseThrow(() -> new DomainException("Customer not found"));

        vehicleRepository.findByVehicleId(command.vehicleId())
                .orElseThrow(() -> new DomainException("Vehicle not found"));

        ServiceOrder serviceOrder = ServiceOrder.open(
                command.customerId(),
                command.vehicleId(),
                command.problemDescription()
        );

        serviceOrderRepository.save(serviceOrder);

        return new ServiceOrderResponse(
                serviceOrder.getServiceOrderId(),
                serviceOrder.getCustomerId(),
                serviceOrder.getVehicleId(),
                serviceOrder.getProblemDescription(),
                serviceOrder.getStatus(),
                serviceOrder.getOpenedAt(),
                serviceOrder.getConcludedAt(),
                serviceOrder.getPriority().name()
        );
    }
}
