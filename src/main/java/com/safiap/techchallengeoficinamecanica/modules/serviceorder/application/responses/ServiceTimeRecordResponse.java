package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceTimeRecord;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceTimeRecordResponse(
        UUID serviceTimeRecordId,
        UUID serviceOrderId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String notes
) {
    public static ServiceTimeRecordResponse from(ServiceTimeRecord record) {
        return new ServiceTimeRecordResponse(
                record.getServiceTimeRecordId(),
                record.getServiceOrderId(),
                record.getStartTime(),
                record.getEndTime(),
                record.getNotes()
        );
    }
}
