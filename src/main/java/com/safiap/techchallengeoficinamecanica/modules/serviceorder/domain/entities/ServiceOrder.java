package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceOrder extends AggregateRoot {

    private UUID serviceOrderId;
    private UUID customerId;
    private UUID vehicleId;
    private String problemDescription;
    private ServiceOrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime concludedAt;
    private ServiceOrderPriority priority;

    private ServiceOrder(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                         String problemDescription, ServiceOrderStatus status,
                         LocalDateTime openedAt, LocalDateTime concludedAt, ServiceOrderPriority priority) {
        this.serviceOrderId = serviceOrderId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
        this.priority = priority;
    }

    public static ServiceOrder open(UUID customerId, UUID vehicleId, String problemDescription) {
        return new ServiceOrder(UUID.randomUUID(), customerId, vehicleId,
                problemDescription, ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, ServiceOrderPriority.LOW);
    }

    public static ServiceOrder build(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                                     String problemDescription, ServiceOrderStatus status,
                                     LocalDateTime openedAt, LocalDateTime concludedAt, ServiceOrderPriority priority) {
        return new ServiceOrder(serviceOrderId, customerId, vehicleId,
                problemDescription, status, openedAt, concludedAt, priority);
    }

    public void increasePriority() {
        this.priority = this.priority.increase();
    }

    public void decreasePriority() {
        this.priority = this.priority.decrease();
    }

    public UUID getServiceOrderId() { return serviceOrderId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getVehicleId() { return vehicleId; }
    public String getProblemDescription() { return problemDescription; }
    public ServiceOrderStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getConcludedAt() { return concludedAt; }
    public ServiceOrderPriority getPriority() { return priority;}
}
