package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.mappers.ServiceOrderMapper;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories.JPAServiceOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final JPAServiceOrderRepository jpaServiceOrderRepository;

    public ServiceOrderRepositoryImpl(JPAServiceOrderRepository jpaServiceOrderRepository) {
        this.jpaServiceOrderRepository = jpaServiceOrderRepository;
    }

    @Override
    public void save(ServiceOrder serviceOrder) {
        jpaServiceOrderRepository.save(ServiceOrderMapper.toJPA(serviceOrder));
    }

    @Override
    public Optional<ServiceOrder> findById(UUID serviceOrderId) {
        return jpaServiceOrderRepository.findById(serviceOrderId)
                .map(ServiceOrderMapper::toEntity);
    }

    @Override
    public List<ServiceOrder> findByCustomerId(UUID customerId) {
        return jpaServiceOrderRepository.findByCustomerId(customerId)
                .stream().map(ServiceOrderMapper::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ServiceOrder> findByVehicleId(UUID vehicleId) {
        return jpaServiceOrderRepository.findByVehicleId(vehicleId)
                .stream().map(ServiceOrderMapper::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ServiceOrder> findByStatus(ServiceOrderStatus status) {
        return jpaServiceOrderRepository.findByStatus(status)
                .stream().map(ServiceOrderMapper::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceOrder> pullNextOrderService(ServiceOrderStatus status) {
        return jpaServiceOrderRepository.findFirstByStatusOrderByPriorityDescOpenedAtAsc(status).
                map(ServiceOrderMapper::toEntity);
    }

    @Override
    public List<ServiceOrder> getAllServiceOrdersFiltered() {
        return jpaServiceOrderRepository.getAllServiceOrdersFiltered()
                .stream().map(ServiceOrderMapper::toEntity).collect(Collectors.toList());
    }
}
