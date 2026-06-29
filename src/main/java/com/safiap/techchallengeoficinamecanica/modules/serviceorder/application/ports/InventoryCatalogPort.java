package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports;

import java.util.UUID;


public interface InventoryCatalogPort {
    void ensurePartExists(UUID partId);
    void ensureServiceExists(UUID serviceId);
}
