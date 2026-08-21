package com.safiap.techchallengeoficinamecanica.modules.notifications.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.NotificationRecipient;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.GetVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer.GetCustomerByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle.GetVehicleByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterNotificationRecipientAdapterTest {

    private final GetCustomerByIdUseCase getCustomerByIdUseCase = mock(GetCustomerByIdUseCase.class);
    private final GetVehicleByIdUseCase getVehicleByIdUseCase = mock(GetVehicleByIdUseCase.class);
    private final RegisterNotificationRecipientAdapter adapter =
            new RegisterNotificationRecipientAdapter(getCustomerByIdUseCase, getVehicleByIdUseCase);

    private final UUID customerId = UUID.randomUUID();
    private final UUID vehicleId = UUID.randomUUID();

    @Test
    @DisplayName("maps the customer into a notification recipient")
    void mapsCustomer() {
        when(getCustomerByIdUseCase.execute(customerId)).thenReturn(new GetCustomerResponse(
                customerId, "João Silva", "joao@email.com", "11999998888", "11144477735"));

        Optional<NotificationRecipient> recipient = adapter.findRecipient(customerId);

        assertThat(recipient).contains(new NotificationRecipient("João Silva", "joao@email.com"));
    }

    @Test
    @DisplayName("returns empty instead of failing when the customer does not exist")
    void returnsEmptyWhenCustomerIsMissing() {
        when(getCustomerByIdUseCase.execute(customerId))
                .thenThrow(new NotFoundException("Customer with id " + customerId + " not found."));

        assertThat(adapter.findRecipient(customerId)).isEmpty();
    }

    @Test
    @DisplayName("builds the vehicle label from manufacturer, model and license plate")
    void buildsVehicleLabel() {
        when(getVehicleByIdUseCase.execute(vehicleId)).thenReturn(new GetVehicleResponse(
                vehicleId, customerId, "ABC1D23", "Civic", "Honda", 45000, Year.of(2020)));

        assertThat(adapter.findVehicleLabel(vehicleId)).contains("Honda Civic (ABC1D23)");
    }

    @Test
    @DisplayName("returns empty instead of failing when the vehicle does not exist")
    void returnsEmptyWhenVehicleIsMissing() {
        when(getVehicleByIdUseCase.execute(vehicleId))
                .thenThrow(new DomainException("Vehicle with id " + vehicleId + " not found."));

        assertThat(adapter.findVehicleLabel(vehicleId)).isEmpty();
    }
}
