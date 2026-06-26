package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceTimeRecord {

    private UUID serviceTimeRecordId;
    private UUID serviceOrderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;

    private ServiceTimeRecord(UUID serviceTimeRecordId, UUID serviceOrderId,
                              LocalDateTime startTime, LocalDateTime endTime, String notes) {
        this.serviceTimeRecordId = serviceTimeRecordId;
        this.serviceOrderId = serviceOrderId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }

    public static ServiceTimeRecord create(UUID serviceOrderId, LocalDateTime startTime,
                                           LocalDateTime endTime, String notes) {
        return new ServiceTimeRecord(UUID.randomUUID(), serviceOrderId, startTime, endTime, notes);
    }

    public static ServiceTimeRecord build(UUID serviceTimeRecordId, UUID serviceOrderId,
                                          LocalDateTime startTime, LocalDateTime endTime, String notes) {
        return new ServiceTimeRecord(serviceTimeRecordId, serviceOrderId, startTime, endTime, notes);
    }

    public UUID getServiceTimeRecordId() { return serviceTimeRecordId; }
    public UUID getServiceOrderId() { return serviceOrderId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getNotes() { return notes; }
}
