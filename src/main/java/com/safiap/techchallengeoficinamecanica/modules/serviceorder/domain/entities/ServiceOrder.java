package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.*;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ServiceOrder extends AggregateRoot {

    private UUID serviceOrderId;
    private UUID customerId;
    private UUID vehicleId;
    private String problemDescription;
    private ServiceOrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime concludedAt;
    private Budget budget;
    private Diagnosis diagnosis;
    private Authorization authorization;
    private List<ExecutedService> executedServices;

    private ServiceOrder() {}

    private ServiceOrder(UUID serviceOrderId, UUID customerId, UUID vehicleId, String problemDescription,
                         ServiceOrderStatus status, LocalDateTime openedAt, LocalDateTime concludedAt,
                         Budget budget, Diagnosis diagnosis, Authorization authorization,
                         List<ExecutedService> executedServices) {
        this.serviceOrderId = serviceOrderId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
        this.budget = budget;
        this.diagnosis = diagnosis;
        this.authorization = authorization;
        this.executedServices = executedServices != null ? new ArrayList<>(executedServices) : new ArrayList<>();
    }

    public static ServiceOrder open(UUID customerId, UUID vehicleId, String problemDescription) {
        Objects.requireNonNull(customerId, "customerId is null");
        Objects.requireNonNull(vehicleId, "vehicleId is null");
        Objects.requireNonNull(problemDescription, "problemDescription is null");

        ServiceOrder so = new ServiceOrder(
                UUID.randomUUID(), customerId, vehicleId, problemDescription,
                ServiceOrderStatus.OPEN, LocalDateTime.now(), null,
                null, null, null, new ArrayList<>()
        );
        so.registerDomainEvent(new ServiceOrderOpenedEvent(so.serviceOrderId, customerId, vehicleId));
        return so;
    }

    public static ServiceOrder build(UUID serviceOrderId, UUID customerId, UUID vehicleId, String problemDescription,
                                     ServiceOrderStatus status, LocalDateTime openedAt, LocalDateTime concludedAt,
                                     Budget budget, Diagnosis diagnosis, Authorization authorization,
                                     List<ExecutedService> executedServices) {
        Objects.requireNonNull(serviceOrderId, "serviceOrderId is null");
        return new ServiceOrder(serviceOrderId, customerId, vehicleId, problemDescription,
                status, openedAt, concludedAt, budget, diagnosis, authorization, executedServices);
    }

    public void createBudget() {
        if (status != ServiceOrderStatus.OPEN) throw new DomainException("Budget can only be created when service order is OPEN");
        if (budget != null) throw new DomainException("A budget already exists for this service order");
        this.budget = Budget.create();
        registerDomainEvent(new BudgetCreatedEvent(serviceOrderId, budget.getBudgetId()));
    }

    public void addBudgetPartItem(UUID partId, String description, Integer quantity, BigDecimal unitPrice) {
        requireBudget();
        budget.addPartItem(BudgetItemPart.create(partId, description, quantity, unitPrice));
    }

    public void addBudgetServiceItem(String description, BigDecimal price) {
        requireBudget();
        budget.addServiceItem(BudgetItemService.create(description, price));
    }

    public void approveBudget() {
        requireBudget();
        budget.approve();
        registerDomainEvent(new BudgetApprovedEvent(serviceOrderId, budget.getBudgetId()));
    }

    public void rejectBudget() {
        requireBudget();
        budget.reject();
        registerDomainEvent(new BudgetRejectedEvent(serviceOrderId, budget.getBudgetId()));
    }

    public void startDiagnosis() {
        if (budget == null || budget.getStatus() != BudgetStatus.APPROVED)
            throw new DomainException("Diagnosis can only start after budget is approved");
        if (status != ServiceOrderStatus.OPEN) throw new DomainException("Diagnosis can only start when service order is OPEN");
        this.diagnosis = Diagnosis.start();
        this.status = ServiceOrderStatus.DIAGNOSIS;
        registerDomainEvent(new DiagnosisStartedEvent(serviceOrderId));
    }

    public void concludeDiagnosis(String notes) {
        if (status != ServiceOrderStatus.DIAGNOSIS) throw new DomainException("Service order is not in DIAGNOSIS status");
        if (diagnosis == null) throw new DomainException("No active diagnosis found");
        diagnosis.conclude(notes);
        this.status = ServiceOrderStatus.AWAITING_AUTHORIZATION;
        registerDomainEvent(new DiagnosisConcludedEvent(serviceOrderId));
    }

    public void registerAuthorization(String notes) {
        if (status != ServiceOrderStatus.AWAITING_AUTHORIZATION)
            throw new DomainException("Authorization can only be registered when service order is AWAITING_AUTHORIZATION");
        this.authorization = Authorization.register(notes);
        this.status = ServiceOrderStatus.AUTHORIZED;
        registerDomainEvent(new ServiceOrderAuthorizedEvent(serviceOrderId));
    }

    public void startExecution() {
        if (status != ServiceOrderStatus.AUTHORIZED)
            throw new DomainException("Execution can only start after authorization is registered");
        this.status = ServiceOrderStatus.IN_EXECUTION;
        registerDomainEvent(new ExecutionStartedEvent(serviceOrderId));
    }

    public void registerExecutedService(String description) {
        if (status != ServiceOrderStatus.IN_EXECUTION)
            throw new DomainException("Service order is not IN_EXECUTION");
        executedServices.add(ExecutedService.register(description));
    }

    public void conclude() {
        if (status != ServiceOrderStatus.IN_EXECUTION)
            throw new DomainException("Service order can only be concluded when IN_EXECUTION");
        this.status = ServiceOrderStatus.CONCLUDED;
        this.concludedAt = LocalDateTime.now();
        registerDomainEvent(new ServiceOrderConcludedEvent(serviceOrderId));
    }

    public void cancel() {
        if (status == ServiceOrderStatus.IN_EXECUTION || status == ServiceOrderStatus.CONCLUDED || status == ServiceOrderStatus.CANCELLED)
            throw new DomainException("Service order cannot be cancelled in status: " + status);
        this.status = ServiceOrderStatus.CANCELLED;
        registerDomainEvent(new ServiceOrderCancelledEvent(serviceOrderId));
    }

    private void requireBudget() {
        if (budget == null) throw new DomainException("No budget found for this service order");
    }

    public UUID getServiceOrderId() { return serviceOrderId; }
    public UUID getCustomerId() { return customerId; }
    public UUID getVehicleId() { return vehicleId; }
    public String getProblemDescription() { return problemDescription; }
    public ServiceOrderStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getConcludedAt() { return concludedAt; }
    public Budget getBudget() { return budget; }
    public Diagnosis getDiagnosis() { return diagnosis; }
    public Authorization getAuthorization() { return authorization; }
    public List<ExecutedService> getExecutedServices() { return Collections.unmodifiableList(executedServices); }
}
