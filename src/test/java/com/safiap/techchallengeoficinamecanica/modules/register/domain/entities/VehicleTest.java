package com.safiap.techchallengeoficinamecanica.modules.register.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CarLicensePlate;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Kilometers;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehicleTest {

    private final UUID customerId = UUID.randomUUID();
    private final CarLicensePlate plate = new CarLicensePlate("ABC1D23");
    private final Kilometers kilometers = new Kilometers(10000);
    private final Year year = Year.of(2020);

    private Vehicle newVehicle() {
        return Vehicle.createVehicle(customerId, plate, "Gol", "Volkswagen", kilometers, year);
    }

    @Test
    @DisplayName("creates a vehicle generating an id")
    void createsVehicleGeneratingId() {
        Vehicle vehicle = newVehicle();

        assertThat(vehicle.getVehicleId()).isNotNull();
        assertThat(vehicle.getCustomerId()).isEqualTo(customerId);
        assertThat(vehicle.getCarLicensePlate()).isEqualTo(plate);
        assertThat(vehicle.getModel()).isEqualTo("Gol");
        assertThat(vehicle.getManufacturer()).isEqualTo("Volkswagen");
        assertThat(vehicle.getKilometers()).isEqualTo(kilometers);
        assertThat(vehicle.getYear()).isEqualTo(year);
    }

    @Test
    @DisplayName("fails to create a vehicle with a null customer")
    void createFailsWhenCustomerIsNull() {
        assertThatThrownBy(() -> Vehicle.createVehicle(null, plate, "Gol", "Volkswagen", kilometers, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("fails to create a vehicle with a null license plate")
    void createFailsWhenPlateIsNull() {
        assertThatThrownBy(() -> Vehicle.createVehicle(customerId, null, "Gol", "Volkswagen", kilometers, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("fails to create a vehicle with a blank model")
    void createFailsWhenModelIsBlank() {
        assertThatThrownBy(() -> Vehicle.createVehicle(customerId, plate, " ", "Volkswagen", kilometers, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("fails to create a vehicle with a blank manufacturer")
    void createFailsWhenManufacturerIsBlank() {
        assertThatThrownBy(() -> Vehicle.createVehicle(customerId, plate, "Gol", " ", kilometers, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("fails to create a vehicle with null kilometers")
    void createFailsWhenKilometersIsNull() {
        assertThatThrownBy(() -> Vehicle.createVehicle(customerId, plate, "Gol", "Volkswagen", null, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("fails to create a vehicle with a null year")
    void createFailsWhenYearIsNull() {
        assertThatThrownBy(() -> Vehicle.createVehicle(customerId, plate, "Gol", "Volkswagen", kilometers, null))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rebuilds a vehicle keeping the provided id")
    void buildsVehicleKeepingProvidedId() {
        UUID vehicleId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.buildVehicle(vehicleId, customerId, plate, "Gol", "Volkswagen", kilometers, year);

        assertThat(vehicle.getVehicleId()).isEqualTo(vehicleId);
    }

    @Test
    @DisplayName("fails to rebuild a vehicle with a null id")
    void buildFailsWhenIdIsNull() {
        assertThatThrownBy(() -> Vehicle.buildVehicle(null, customerId, plate, "Gol", "Volkswagen", kilometers, year))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("updates kilometers to a higher value")
    void increasesKilometers() {
        Vehicle vehicle = newVehicle();

        vehicle.changeKilometers(new Kilometers(20000));

        assertThat(vehicle.getKilometers().value()).isEqualTo(20000);
    }

    @Test
    @DisplayName("fails when kilometers decrease")
    void failsWhenKilometersDecrease() {
        Vehicle vehicle = newVehicle();

        assertThatThrownBy(() -> vehicle.changeKilometers(new Kilometers(5000)))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("alters vehicle data")
    void altersVehicleData() {
        Vehicle vehicle = newVehicle();
        CarLicensePlate newPlate = new CarLicensePlate("XYZ9A88");
        Kilometers newKilometers = new Kilometers(30000);
        Year newYear = Year.of(2022);

        vehicle.alterVehicle(newPlate, "Polo", "VW", newKilometers, newYear);

        assertThat(vehicle.getCarLicensePlate()).isEqualTo(newPlate);
        assertThat(vehicle.getModel()).isEqualTo("Polo");
        assertThat(vehicle.getManufacturer()).isEqualTo("VW");
        assertThat(vehicle.getKilometers()).isEqualTo(newKilometers);
        assertThat(vehicle.getYear()).isEqualTo(newYear);
    }

    @Test
    @DisplayName("fails to alter a vehicle decreasing kilometers")
    void alterFailsWhenKilometersDecrease() {
        Vehicle vehicle = newVehicle();

        assertThatThrownBy(() -> vehicle.alterVehicle(plate, "Polo", "VW", new Kilometers(5000), year))
                .isInstanceOf(DomainException.class);
    }
}
