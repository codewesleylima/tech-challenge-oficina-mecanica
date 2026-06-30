package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Vehicle;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.VehicleRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.OpenServiceOrderCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OpenServiceOrderUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);

    private final OpenServiceOrderUseCase useCase =
            new OpenServiceOrderUseCase(serviceOrderRepository, customerRepository, vehicleRepository, domainEventPublisher);

    @Test
    @DisplayName("opens the service order when customer and vehicle exist")
    void opensOrderWhenCustomerAndVehicleExist() {
        UUID customerId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(mock(Customer.class)));
        when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(Optional.of(mock(Vehicle.class)));

        ServiceOrderResponse response = useCase.execute(
                new OpenServiceOrderCommand(customerId, vehicleId, "Barulho ao frear"));

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.RECEIVED);
        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.vehicleId()).isEqualTo(vehicleId);
        verify(serviceOrderRepository, times(1)).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("fails to open the service order when the customer does not exist")
    void failsWhenCustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(
                new OpenServiceOrderCommand(customerId, UUID.randomUUID(), "x")))
                .isInstanceOf(NotFoundException.class);

        verify(serviceOrderRepository, org.mockito.Mockito.never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("fails to open the service order when the vehicle does not exist")
    void failsWhenVehicleNotFound() {
        UUID customerId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(mock(Customer.class)));
        when(vehicleRepository.findByVehicleId(vehicleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(
                new OpenServiceOrderCommand(customerId, vehicleId, "x")))
                .isInstanceOf(NotFoundException.class);
    }
}
