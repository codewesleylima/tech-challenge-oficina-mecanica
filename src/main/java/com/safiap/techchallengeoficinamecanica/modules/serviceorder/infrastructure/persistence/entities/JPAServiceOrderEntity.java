package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_orders")
@Getter
@NoArgsConstructor
public class JPAServiceOrderEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID vehicleId;

    @Column(nullable = false)
    private String problemDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceOrderStatus status;

    @Column(nullable = false)
    private LocalDateTime openedAt;

    @Column
    private LocalDateTime concludedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "budget_id")
    private JPABudgetEntity budget;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diagnosis_id")
    private JPADiagnosisEntity diagnosis;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "authorization_id")
    private JPAAuthorizationEntity authorization;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_order_id")
    private List<JPAExecutedServiceEntity> executedServices = new ArrayList<>();

    public JPAServiceOrderEntity(UUID id, UUID customerId, UUID vehicleId, String problemDescription,
                                  ServiceOrderStatus status, LocalDateTime openedAt, LocalDateTime concludedAt,
                                  JPABudgetEntity budget, JPADiagnosisEntity diagnosis,
                                  JPAAuthorizationEntity authorization, List<JPAExecutedServiceEntity> executedServices) {
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
        this.budget = budget;
        this.diagnosis = diagnosis;
        this.authorization = authorization;
        this.executedServices = executedServices != null ? executedServices : new ArrayList<>();
    }
}
