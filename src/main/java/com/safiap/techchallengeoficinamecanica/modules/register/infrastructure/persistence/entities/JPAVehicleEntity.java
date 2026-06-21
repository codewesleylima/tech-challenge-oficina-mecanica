package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name="vehicles")
@Getter
@NoArgsConstructor
public class JPAVehicleEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private String carLicensePlate;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private Integer kilometers;
    @Column(nullable = false)
    private Year year;

    public JPAVehicleEntity(UUID customerId,String carLicensePlate, String model, String manufacturer, Integer kilometers, Year year) {
        this.customerId = customerId;
        this.carLicensePlate = carLicensePlate;
        this.model = model;
        this.manufacturer = manufacturer;
        this.kilometers = kilometers;
        this.year = year;
    }

    public JPAVehicleEntity(UUID vehicleId,UUID customerId,String carLicensePlate, String model, String manufacturer, Integer kilometers, Year year) {
        this.id = vehicleId;
        this.customerId = customerId;
        this.carLicensePlate = carLicensePlate;
        this.model = model;
        this.manufacturer = manufacturer;
        this.kilometers = kilometers;
        this.year = year;
    }
}
