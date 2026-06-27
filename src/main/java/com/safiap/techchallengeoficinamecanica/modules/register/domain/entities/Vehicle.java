package com.safiap.techchallengeoficinamecanica.modules.register.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.time.Year;
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

        DomainException.requireNonNull(customerId, " customerId is null");
        DomainException.requireNonNull(carLicensePlate, " carLicensePlate is null");
        DomainException.requireNotBlank(model, " model is blank");
        DomainException.requireNotBlank(manufacturer, " manufacturer is blank");
        DomainException.requireNonNull(kilometers,    " kilometers is null");
        DomainException.requireNonNull(year, " year is null");

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

        DomainException.requireNonNull(vehicleId, " vehicleId is null");
        DomainException.requireNonNull(customerId, " customerId is null");
        DomainException.requireNonNull(carLicensePlate, " carLicensePlate is null");
        DomainException.requireNotBlank(model, " model is blank");
        DomainException.requireNotBlank(manufacturer, " manufacturer is blank");
        DomainException.requireNonNull(kilometers,    " kilometers is null");
        DomainException.requireNonNull(year, " year is null");

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

}
