package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports;

import java.math.BigDecimal;
import java.util.UUID;


public interface InventoryCatalogPort {
    BigDecimal getPartPrice(UUID partId);
    BigDecimal getServicePrice(UUID serviceId);
}
