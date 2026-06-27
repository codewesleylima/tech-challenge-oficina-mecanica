package com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.entities.JpaPartEntity;

public class PartMapper {

    public static JpaPartEntity toJPA(Part part) {
        return new JpaPartEntity(
                part.getId(),
                part.getName(),
                part.getDescription(),
                part.getPrice().amount(),
                part.getQuantity().value()
        );
    }

    public static Part toEntity(JpaPartEntity entity) {
        return Part.buildPart(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new Money(entity.getPrice()),
                new Quantity(entity.getQuantity())
        );
    }
}
