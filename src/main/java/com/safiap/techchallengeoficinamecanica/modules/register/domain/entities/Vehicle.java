package com.safiap.techchallengeoficinamecanica.modules.register.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.time.Year;
import java.util.Objects;
import java.util.UUID;

public class Vehicle extends AggregateRoot {

    private UUID vehicleId;
    private UUID customerId;
    private CarLicensePlate carLicensePlate;
    private String model;
    private String manufacturer;
    private Kilometers kilometers;
    private Year year;

    private Vehicle () {};

    public UUID getVehicleId() {
        return vehicleId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public CarLicensePlate getCarLicensePlate() {
        return carLicensePlate;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Kilometers getKilometers() {
        return kilometers;
    }

    public Year getYear() {
        return year;
    }


    private Vehicle(         UUID vehicleId,
                             UUID customerId,
                             CarLicensePlate carLicensePlate,
                             String model,
                             String manufacturer,
                             Kilometers kilometers,
                             Year year) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.carLicensePlate = carLicensePlate;
        this.model = model;
        this.manufacturer = manufacturer;
        this.kilometers = kilometers;
        this.year = year;
    };

    public static Vehicle createVehicle( UUID customerId,
                                         CarLicensePlate carLicensePlate,
                                         String model,
                                         String manufacturer,
                                         Kilometers kilometers,
                                         Year year) {

        Objects.requireNonNull(customerId, " customerId is null");
        Objects.requireNonNull(carLicensePlate, " carLicensePlate is null");
        Objects.requireNonNull(model,  " model is null");
        Objects.requireNonNull(manufacturer,   " manufacturer is null");
        Objects.requireNonNull(kilometers,    " kilometers is null");
        Objects.requireNonNull(year, " year is null");

        return new Vehicle(
                UUID.randomUUID(),
                customerId,
                carLicensePlate,
                model,
                manufacturer,
                kilometers,
                year
        ) ;
    }


    public static Vehicle buildVehicle(  UUID vehicleId,
                                         UUID customerId,
                                         CarLicensePlate carLicensePlate,
                                         String model,
                                         String manufacturer,
                                         Kilometers kilometers,
                                         Year year) {

        Objects.requireNonNull(vehicleId, " vehicleId is null");
        Objects.requireNonNull(customerId, " customerId is null");
        Objects.requireNonNull(carLicensePlate, " carLicensePlate is null");
        Objects.requireNonNull(model,  " model is null");
        Objects.requireNonNull(manufacturer,   " manufacturer is null");
        Objects.requireNonNull(kilometers,    " kilometers is null");
        Objects.requireNonNull(year, " year is null");

        return new Vehicle(
                vehicleId,
                customerId,
                carLicensePlate,
                model,
                manufacturer,
                kilometers,
                year
        ) ;
    }

    public void changeKilometers(Kilometers kilometers) {
        if (this.kilometers.value() > kilometers.value()) {
            throw new DomainException("Kilometers cannot decrease.");
        }

        this.kilometers = kilometers;
    }

    public void alterVehicle(CarLicensePlate carLicensePlate, String model, String manufactures, Kilometers kilometers, Year year) {
        changeKilometers(kilometers);
        this.carLicensePlate = carLicensePlate;
        this.model = model;
        this.manufacturer = manufactures;
        this.year = year;
    }

}
