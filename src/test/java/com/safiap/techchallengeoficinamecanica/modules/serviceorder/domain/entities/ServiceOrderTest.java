package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.Diagnosis;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceOrderTest {

    private ServiceOrder newOrder() {
        return ServiceOrder.open(UUID.randomUUID(), UUID.randomUUID(), "Barulho ao frear");
    }

    @Test
    void opensWithReceivedStatusAndLowPriority() {
        ServiceOrder order = newOrder();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.RECEIVED);
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(order.getOpenedAt()).isNotNull();
        assertThat(order.getConcludedAt()).isNull();
        assertThat(order.getDiagnosis()).isNull();
    }

    @Test
    void walksThroughTheFullHappyPath() {
        ServiceOrder order = newOrder();

        order.startDiagnosis();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.IN_DIAGNOSIS);

        order.finalizeDiagnosis(new Diagnosis("Pastilhas gastas"));
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);
        assertThat(order.getDiagnosis().value()).isEqualTo("Pastilhas gastas");

        order.startExecution();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.IN_EXECUTION);

        order.finalizeOrder();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.FINALIZED);
        assertThat(order.getConcludedAt()).isNotNull();

        order.deliver();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.DELIVERED);
    }

    @Test
    void startDiagnosisFailsWhenNotReceived() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        assertThatThrownBy(order::startDiagnosis).isInstanceOf(ConflictException.class);
    }

    @Test
    void finalizeDiagnosisFailsWhenNotInDiagnosis() {
        ServiceOrder order = newOrder();
        assertThatThrownBy(() -> order.finalizeDiagnosis(new Diagnosis("x")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void finalizeDiagnosisRequiresDiagnosis() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        assertThatThrownBy(() -> order.finalizeDiagnosis(null))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectBudgetReturnsToDiagnosis() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        order.finalizeDiagnosis(new Diagnosis("ok"));
        order.rejectBudget();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.IN_DIAGNOSIS);
    }

    @Test
    void deliverFailsWhenNotFinalized() {
        ServiceOrder order = newOrder();
        assertThatThrownBy(order::deliver).isInstanceOf(ConflictException.class);
    }

    @Test
    void priorityIncreasesAndDecreasesWithinBounds() {
        ServiceOrder order = newOrder(); // LOW
        order.decreasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.LOW); // cannot go below LOW

        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.NORMAL);
        order.increasePriority();
        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.URGENT);
        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.URGENT); // capped at URGENT
    }
}
