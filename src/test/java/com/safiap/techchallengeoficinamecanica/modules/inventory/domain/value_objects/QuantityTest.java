package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {

    @Test
    void shouldCreateWithGivenValue() {
        assertThat(new Quantity(5).value()).isEqualTo(5);
    }

    @Test
    void shouldAcceptZero() {
        assertThat(new Quantity(0).value()).isZero();
    }

    @Test
    void shouldRejectNegativeValue() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void addShouldReturnIncreasedQuantity() {
        assertThat(new Quantity(5).add(3).value()).isEqualTo(8);
    }

    @Test
    void addShouldRejectNonPositiveAmount() {
        assertThatThrownBy(() -> new Quantity(5).add(0)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Quantity(5).add(-2)).isInstanceOf(DomainException.class);
    }

    @Test
    void subtractShouldReturnDecreasedQuantity() {
        assertThat(new Quantity(5).subtract(2).value()).isEqualTo(3);
    }

    @Test
    void subtractShouldRejectNonPositiveAmount() {
        assertThatThrownBy(() -> new Quantity(5).subtract(0)).isInstanceOf(DomainException.class);
    }

    @Test
    void subtractShouldRejectInsufficientStock() {
        assertThatThrownBy(() -> new Quantity(5).subtract(10))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("insufficient");
    }

    @Test
    void operationsShouldBeImmutable() {
        Quantity quantity = new Quantity(5);
        quantity.add(3);
        quantity.subtract(1);
        assertThat(quantity.value()).isEqualTo(5);
    }
}
