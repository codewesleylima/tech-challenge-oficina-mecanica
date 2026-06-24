package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPAServiceOrderEntity;

public class ServiceOrderMapper {

    public static JPAServiceOrderEntity toJPA(ServiceOrder serviceOrder) {
        return new JPAServiceOrderEntity(
                serviceOrder.getServiceOrderId(),
                serviceOrder.getCustomerId(),
                serviceOrder.getVehicleId(),
                serviceOrder.getProblemDescription(),
                serviceOrder.getStatus(),
                serviceOrder.getOpenedAt(),
                serviceOrder.getConcludedAt()
        );
    }

    public static ServiceOrder toEntity(JPAServiceOrderEntity entity) {
        return ServiceOrder.build(
                entity.getId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getProblemDescription(),
                entity.getStatus(),
                entity.getOpenedAt(),
                entity.getConcludedAt()
        );
    }
}
