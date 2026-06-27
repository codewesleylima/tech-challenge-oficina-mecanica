package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.Diagnosis;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceOrder extends AggregateRoot {

    private UUID serviceOrderId;
    private UUID customerId;
    private UUID vehicleId;
    private String problemDescription;
    private Diagnosis diagnosis;
    private ServiceOrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime concludedAt;
    private ServiceOrderPriority priority;

    private ServiceOrder(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                         String problemDescription, Diagnosis diagnosis, ServiceOrderStatus status,
                         LocalDateTime openedAt, LocalDateTime concludedAt, ServiceOrderPriority priority) {
        this.serviceOrderId = serviceOrderId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.diagnosis = diagnosis;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
        this.priority = priority;
    }

    public static ServiceOrder open(UUID customerId, UUID vehicleId, String problemDescription) {
        return new ServiceOrder(UUID.randomUUID(), customerId, vehicleId,
                problemDescription, null, ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, ServiceOrderPriority.LOW);
    }

    public static ServiceOrder build(UUID serviceOrderId, UUID customerId, UUID vehicleId,
                                     String problemDescription, Diagnosis diagnosis, ServiceOrderStatus status,
                                     LocalDateTime openedAt, LocalDateTime concludedAt, ServiceOrderPriority priority) {
        return new ServiceOrder(serviceOrderId, customerId, vehicleId,
                problemDescription, diagnosis, status, openedAt, concludedAt, priority);
    }

    public void increasePriority() {
        this.priority = this.priority.increase();
    }

    public void decreasePriority() {
        this.priority = this.priority.decrease();
    }

    public void startDiagnosis() {
        if (this.status != ServiceOrderStatus.RECEIVED)
            throw new ConflictException("Service order must be in RECEIVED status to start diagnosis");
        this.status = ServiceOrderStatus.IN_DIAGNOSIS;
    }

    public void finalizeDiagnosis(Diagnosis diagnosis) {
        if (this.status != ServiceOrderStatus.IN_DIAGNOSIS)
            throw new ConflictException("Service order must be in IN_DIAGNOSIS status to finalize diagnosis");
        if (diagnosis == null)
            throw new DomainException("Diagnosis description is required to finalize diagnosis");
        this.diagnosis = diagnosis;
        this.status = ServiceOrderStatus.AWAITING_APPROVAL;
    }

    public void startExecution() {
        if (this.status != ServiceOrderStatus.AWAITING_APPROVAL)
            throw new ConflictException("Service order must be in AWAITING_APPROVAL status to start execution");
        this.status = ServiceOrderStatus.IN_EXECUTION;
    }

    public void rejectBudget() {
        if (this.status != ServiceOrderStatus.AWAITING_APPROVAL)
            throw new ConflictException("Service order must be in AWAITING_APPROVAL status to reject budget");
        this.status = ServiceOrderStatus.IN_DIAGNOSIS;
    }

    public void finalizeOrder() {
        if (this.status != ServiceOrderStatus.IN_EXECUTION)
            throw new ConflictException("Service order must be in IN_EXECUTION status to finalize");
        this.status = ServiceOrderStatus.FINALIZED;
        this.concludedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status != ServiceOrderStatus.FINALIZED)
            throw new ConflictException("Service order must be in FINALIZED status to deliver");
        this.status = ServiceOrderStatus.DELIVERED;
    }

    public UUID getServiceOrderId() { return serviceOrderId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getVehicleId() { return vehicleId; }
    public String getProblemDescription() { return problemDescription; }
    public Diagnosis getDiagnosis() { return diagnosis; }
    public ServiceOrderStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getConcludedAt() { return concludedAt; }
    public ServiceOrderPriority getPriority() { return priority;}
}
