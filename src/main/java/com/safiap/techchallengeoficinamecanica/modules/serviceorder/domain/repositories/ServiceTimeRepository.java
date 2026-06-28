package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceTimeRecord;

import java.util.List;
import java.util.UUID;

public interface ServiceTimeRepository {
    void save(ServiceTimeRecord record);
    List<ServiceTimeRecord> findByServiceOrderId(UUID serviceOrderId);
}
