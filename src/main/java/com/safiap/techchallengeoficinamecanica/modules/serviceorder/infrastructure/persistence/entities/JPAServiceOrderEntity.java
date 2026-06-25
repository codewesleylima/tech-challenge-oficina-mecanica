package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime concludedAt;

    private int priority=1;

    public JPAServiceOrderEntity(UUID id, UUID customerId, UUID vehicleId,
                                 String problemDescription, ServiceOrderStatus status,
                                 LocalDateTime openedAt, LocalDateTime concludedAt, ServiceOrderPriority priority) {
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.problemDescription = problemDescription;
        this.status = status;
        this.openedAt = openedAt;
        this.concludedAt = concludedAt;
        this.priority=priority.getValue();
    }
}
