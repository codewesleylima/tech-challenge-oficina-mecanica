package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KilometersTest {

    @Test
    @DisplayName("accepts a positive value")
    void acceptsPositiveValue() {
        Kilometers kilometers = new Kilometers(1000);
        assertThat(kilometers.value()).isEqualTo(1000);
    }

    @Test
    @DisplayName("accepts zero")
    void acceptsZero() {
        Kilometers kilometers = new Kilometers(0);
        assertThat(kilometers.value()).isZero();
    }

    @Test
    @DisplayName("rejects a null value")
    void rejectsNull() {
        assertThatThrownBy(() -> new Kilometers(null)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejects a negative value")
    void rejectsNegativeValue() {
        assertThatThrownBy(() -> new Kilometers(-1)).isInstanceOf(DomainException.class);
    }
}
