package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities.JPAServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPAServiceOrderRepository extends JpaRepository<JPAServiceOrderEntity, UUID> {
    List<JPAServiceOrderEntity> findByCustomerId(UUID customerId);
    List<JPAServiceOrderEntity> findByVehicleId(UUID vehicleId);
    List<JPAServiceOrderEntity> findByStatus(ServiceOrderStatus status);
    Optional<JPAServiceOrderEntity> findFirstByStatusOrderByPriorityDescOpenedAtAsc(ServiceOrderStatus status);
    @Query(value = """
    SELECT * FROM service_orders
    WHERE status NOT IN ('FINALIZED', 'DELIVERED')
    ORDER BY
        CASE status
            WHEN 'IN_EXECUTION'      THEN 1
            WHEN 'AWAITING_APPROVAL' THEN 2
            WHEN 'IN_DIAGNOSIS'      THEN 3
            WHEN 'RECEIVED'          THEN 4
            ELSE 5
        END,
        opened_at ASC
    """, nativeQuery = true)
    List<JPAServiceOrderEntity> getAllServiceOrdersFiltered();
}

