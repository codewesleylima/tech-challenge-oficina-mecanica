package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceOrderPriorityTest {

    @Test
    @DisplayName("teste aumentar a prioridade para no máximo URGENT")
    void increaseStopsAtUrgent() {
        assertThat(ServiceOrderPriority.LOW.increase()).isEqualTo(ServiceOrderPriority.NORMAL);
        assertThat(ServiceOrderPriority.NORMAL.increase()).isEqualTo(ServiceOrderPriority.HIGH);
        assertThat(ServiceOrderPriority.HIGH.increase()).isEqualTo(ServiceOrderPriority.URGENT);
        assertThat(ServiceOrderPriority.URGENT.increase()).isEqualTo(ServiceOrderPriority.URGENT);
    }

    @Test
    @DisplayName("teste diminuir a prioridade para no mínimo LOW")
    void decreaseStopsAtLow() {
        assertThat(ServiceOrderPriority.URGENT.decrease()).isEqualTo(ServiceOrderPriority.HIGH);
        assertThat(ServiceOrderPriority.HIGH.decrease()).isEqualTo(ServiceOrderPriority.NORMAL);
        assertThat(ServiceOrderPriority.NORMAL.decrease()).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(ServiceOrderPriority.LOW.decrease()).isEqualTo(ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("teste resolve a prioridade a partir do valor numérico conhecido")
    void fromValueResolvesKnownValues() {
        assertThat(ServiceOrderPriority.fromValue(1)).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(ServiceOrderPriority.fromValue(4)).isEqualTo(ServiceOrderPriority.URGENT);
    }

    @Test
    @DisplayName("teste falha ao resolver a prioridade a partir de valor desconhecido")
    void fromValueFailsForUnknownValue() {
        assertThatThrownBy(() -> ServiceOrderPriority.fromValue(99))
                .isInstanceOf(NotFoundException.class);
    }
}
