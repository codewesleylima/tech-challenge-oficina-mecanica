package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("normalizes the scale to 2 places using HALF_UP")
    void shouldNormalizeScaleToTwoDecimals() {
        Money money = new Money(new BigDecimal("10.005"));

        assertThat(money.amount()).isEqualByComparingTo("10.01");
        assertThat(money.amount().scale()).isEqualTo(2);
    }

    @Test
    void shouldConsiderZeroAsZero() {
        assertThat(new Money(BigDecimal.ZERO).isZero()).isTrue();
    }

    @Test
    void shouldNotConsiderPositiveAsZero() {
        assertThat(new Money(new BigDecimal("1.00")).isZero()).isFalse();
    }

    @Test
    void shouldRejectNullAmount() {
        assertThatThrownBy(() -> new Money(null))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-0.01")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("negative");
    }
}
