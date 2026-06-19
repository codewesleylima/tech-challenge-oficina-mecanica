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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private String CarLicensePlate;
    @Column(nullable = false)
    private String Model;
    @Column(nullable = false)
    private String Manufacturer;
    @Column(nullable = false)
    private Integer kilometers;
    @Column(nullable = false)
    private Year year;

    public JPAVehicleEntity(UUID customerId,String carLicensePlate, String model, String manufacturer, Integer kilometers, Year year) {
        this.customerId = customerId;
        this.CarLicensePlate = carLicensePlate;
        this.Model = model;
        this.Manufacturer = manufacturer;
        this.kilometers = kilometers;
        this.year = year;
    }
}
