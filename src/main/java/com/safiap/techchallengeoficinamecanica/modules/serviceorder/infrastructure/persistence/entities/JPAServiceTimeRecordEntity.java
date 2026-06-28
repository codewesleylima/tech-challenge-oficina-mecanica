package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_time_records")
@Getter
@NoArgsConstructor
public class JPAServiceTimeRecordEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID serviceOrderId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private String notes;

    public JPAServiceTimeRecordEntity(UUID id, UUID serviceOrderId,
                                      LocalDateTime startTime, LocalDateTime endTime, String notes) {
        this.id = id;
        this.serviceOrderId = serviceOrderId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }
}
