package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceTimeRecord;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceTimeRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers.ServiceTimeMapper;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories.JPAServiceTimeRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ServiceTimeRepositoryImpl implements ServiceTimeRepository {

    private final JPAServiceTimeRecordRepository jpaRepository;

    public ServiceTimeRepositoryImpl(JPAServiceTimeRecordRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(ServiceTimeRecord record) {
        jpaRepository.save(ServiceTimeMapper.toJPA(record));
    }

    @Override
    public List<ServiceTimeRecord> findByServiceOrderId(UUID serviceOrderId) {
        return jpaRepository.findByServiceOrderId(serviceOrderId)
                .stream().map(ServiceTimeMapper::toEntity).collect(Collectors.toList());
    }
}
