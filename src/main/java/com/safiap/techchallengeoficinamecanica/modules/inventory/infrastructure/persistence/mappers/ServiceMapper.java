package com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.entities.JpaServiceEntity;

public class ServiceMapper {

    public static JpaServiceEntity toJPA(Service service) {
        return new JpaServiceEntity(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice().amount()
        );
    }

    public static Service toEntity(JpaServiceEntity entity) {
        return Service.buildService(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new Money(entity.getPrice())
        );
    }
}
