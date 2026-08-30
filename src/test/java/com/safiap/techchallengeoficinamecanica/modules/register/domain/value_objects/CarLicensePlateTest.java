package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarLicensePlateTest {

    @Test
    @DisplayName("accepts a Mercosul-pattern plate")
    void acceptsMercosulFormat() {
        CarLicensePlate plate = new CarLicensePlate("ABC1D23");
        assertThat(plate.plate()).isEqualTo("ABC1D23");
    }

    @Test
    @DisplayName("transform a lowercase plate to uppercase plate")
    void transformLowercaseToUppercase() {
        CarLicensePlate plate = new CarLicensePlate("abc1d23");
        assertThat(plate.plate()).isEqualTo("ABC1D23");
    }

    @Test
    @DisplayName("rejects a null plate")
    void rejectsNull() {
        assertThatThrownBy(() -> new CarLicensePlate(null)).isInstanceOf(DomainException.class);
    }
}
