package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceTimeRecord;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPAServiceTimeRecordEntity;

public class ServiceTimeMapper {

    public static JPAServiceTimeRecordEntity toJPA(ServiceTimeRecord record) {
        return new JPAServiceTimeRecordEntity(
                record.getServiceTimeRecordId(),
                record.getServiceOrderId(),
                record.getStartTime(),
                record.getEndTime(),
                record.getNotes()
        );
    }

    public static ServiceTimeRecord toEntity(JPAServiceTimeRecordEntity entity) {
        return ServiceTimeRecord.build(
                entity.getId(),
                entity.getServiceOrderId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getNotes()
        );
    }
}
