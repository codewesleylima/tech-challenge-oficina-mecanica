package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository {
    void save(ServiceOrder serviceOrder);
    Optional<ServiceOrder> findById(UUID serviceOrderId);
    List<ServiceOrder> findByCustomerId(UUID customerId);
    List<ServiceOrder> findByVehicleId(UUID vehicleId);
    List<ServiceOrder> findByStatus(ServiceOrderStatus status);
    Optional<ServiceOrder> pullNextOrderService(ServiceOrderStatus status);
}
