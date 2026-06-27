package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceOrderPriorityTest {

    @Test
    void increaseStopsAtUrgent() {
        assertThat(ServiceOrderPriority.LOW.increase()).isEqualTo(ServiceOrderPriority.NORMAL);
        assertThat(ServiceOrderPriority.NORMAL.increase()).isEqualTo(ServiceOrderPriority.HIGH);
        assertThat(ServiceOrderPriority.HIGH.increase()).isEqualTo(ServiceOrderPriority.URGENT);
        assertThat(ServiceOrderPriority.URGENT.increase()).isEqualTo(ServiceOrderPriority.URGENT);
    }

    @Test
    void decreaseStopsAtLow() {
        assertThat(ServiceOrderPriority.URGENT.decrease()).isEqualTo(ServiceOrderPriority.HIGH);
        assertThat(ServiceOrderPriority.HIGH.decrease()).isEqualTo(ServiceOrderPriority.NORMAL);
        assertThat(ServiceOrderPriority.NORMAL.decrease()).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(ServiceOrderPriority.LOW.decrease()).isEqualTo(ServiceOrderPriority.LOW);
    }

    @Test
    void fromValueResolvesKnownValues() {
        assertThat(ServiceOrderPriority.fromValue(1)).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(ServiceOrderPriority.fromValue(4)).isEqualTo(ServiceOrderPriority.URGENT);
    }

    @Test
    void fromValueFailsForUnknownValue() {
        assertThatThrownBy(() -> ServiceOrderPriority.fromValue(99))
                .isInstanceOf(NotFoundException.class);
    }
}
