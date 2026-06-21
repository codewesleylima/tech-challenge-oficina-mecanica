package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    private UUID vehicleIdEntity;

    @Column(name = "customer_id", nullable = false)
    private UUID customerIdEntity;

    @Column(name = "car_license_plate", nullable = false, unique = true)
    private String carLicensePlateEntity;

    @Column(nullable = false)
    private String modelEntity;

    @Column(nullable = false)
    private String manufacturerEntity;

    @Column(nullable = false)
    private Integer kilometersEntity;

    @Column(nullable = false)
    private Integer yearEntity;
}
