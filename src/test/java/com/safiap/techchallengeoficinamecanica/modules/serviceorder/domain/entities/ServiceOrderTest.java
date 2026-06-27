package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.Diagnosis;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceOrderTest {

    private ServiceOrder newOrder() {
        return ServiceOrder.open(UUID.randomUUID(), UUID.randomUUID(), "Barulho ao frear");
    }

    @Test
    @DisplayName("teste abre a ordem de serviço com status RECEIVED e prioridade LOW")
    void opensWithReceivedStatusAndLowPriority() {
        ServiceOrder order = newOrder();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.RECEIVED);
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.LOW);
        assertThat(order.getOpenedAt()).isNotNull();
        assertThat(order.getConcludedAt()).isNull();
        assertThat(order.getDiagnosis()).isNull();
    }

    @Test
    @DisplayName("teste percorre o fluxo completo de status até DELIVERED")
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
    @DisplayName("teste falha ao iniciar diagnóstico fora do status RECEIVED")
    void startDiagnosisFailsWhenNotReceived() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        assertThatThrownBy(order::startDiagnosis).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste falha ao finalizar diagnóstico fora do status IN_DIAGNOSIS")
    void finalizeDiagnosisFailsWhenNotInDiagnosis() {
        ServiceOrder order = newOrder();
        assertThatThrownBy(() -> order.finalizeDiagnosis(new Diagnosis("x")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste exige a descrição do diagnóstico ao finalizar o diagnóstico")
    void finalizeDiagnosisRequiresDiagnosis() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        assertThatThrownBy(() -> order.finalizeDiagnosis(null))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("teste rejeitar orçamento retorna a ordem de serviço para IN_DIAGNOSIS")
    void rejectBudgetReturnsToDiagnosis() {
        ServiceOrder order = newOrder();
        order.startDiagnosis();
        order.finalizeDiagnosis(new Diagnosis("ok"));
        order.rejectBudget();
        assertThat(order.getStatus()).isEqualTo(ServiceOrderStatus.IN_DIAGNOSIS);
    }

    @Test
    @DisplayName("teste falha ao entregar a ordem de serviço que não está FINALIZED")
    void deliverFailsWhenNotFinalized() {
        ServiceOrder order = newOrder();
        assertThatThrownBy(order::deliver).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste aumenta e diminui a prioridade respeitando os limites LOW e URGENT")
    void priorityIncreasesAndDecreasesWithinBounds() {
        ServiceOrder order = newOrder();
        order.decreasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.LOW);

        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.NORMAL);
        order.increasePriority();
        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.URGENT);
        order.increasePriority();
        assertThat(order.getPriority()).isEqualTo(ServiceOrderPriority.URGENT);
    }
}
