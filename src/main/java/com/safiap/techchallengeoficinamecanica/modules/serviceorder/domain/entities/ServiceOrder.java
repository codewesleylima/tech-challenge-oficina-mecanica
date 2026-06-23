package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

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

    private ServiceOrder(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                         String problemDescription, ServiceOrderStatus status,
                         LocalDateTime openedAt, LocalDateTime concludedAt) {
        this.serviceOrderId = serviceOrderId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
    }

    public static ServiceOrder open(UUID customerId, UUID vehicleId, String problemDescription) {
        return new ServiceOrder(UUID.randomUUID(), customerId, vehicleId,
                problemDescription, ServiceOrderStatus.OPEN, LocalDateTime.now(), null);
    }

    public static ServiceOrder build(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                                     String problemDescription, ServiceOrderStatus status,
                                     LocalDateTime openedAt, LocalDateTime concludedAt) {
        return new ServiceOrder(serviceOrderId, customerId, vehicleId,
                problemDescription, status, openedAt, concludedAt);
    }

    public void cancel() {
        if (this.status == ServiceOrderStatus.CONCLUDED || this.status == ServiceOrderStatus.CANCELLED) {
            throw new DomainException("Service order cannot be cancelled in its current status");
        }
        this.status = ServiceOrderStatus.CANCELLED;
    }

    public UUID getServiceOrderId() { return serviceOrderId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getVehicleId() { return vehicleId; }
    public String getProblemDescription() { return problemDescription; }
    public ServiceOrderStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getConcludedAt() { return concludedAt; }
}
