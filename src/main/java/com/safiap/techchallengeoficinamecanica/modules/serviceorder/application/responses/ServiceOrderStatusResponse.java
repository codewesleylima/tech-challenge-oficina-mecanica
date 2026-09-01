package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;

public record ServiceOrderStatusResponse(
        ServiceOrderStatus status
) {
    public static ServiceOrderStatusResponse from(ServiceOrder serviceOrder) {
        return new ServiceOrderStatusResponse(
                serviceOrder.getStatus()
        );
    }
}
