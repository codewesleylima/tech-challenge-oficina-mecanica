package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports;

import java.util.UUID;


public interface PartStockPort {
    void decreaseStock(UUID partId, int quantity);
}
