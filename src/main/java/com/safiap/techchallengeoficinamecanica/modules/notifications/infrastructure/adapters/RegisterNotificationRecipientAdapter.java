package com.safiap.techchallengeoficinamecanica.modules.notifications.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.NotificationRecipientPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.NotificationRecipient;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.vehicle.GetVehicleResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer.GetCustomerByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.vehicle.GetVehicleByIdUseCase;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RegisterNotificationRecipientAdapter implements NotificationRecipientPort {

    private static final Logger log = LoggerFactory.getLogger(RegisterNotificationRecipientAdapter.class);

    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final GetVehicleByIdUseCase getVehicleByIdUseCase;

    public RegisterNotificationRecipientAdapter(GetCustomerByIdUseCase getCustomerByIdUseCase,
                                                GetVehicleByIdUseCase getVehicleByIdUseCase) {
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
        this.getVehicleByIdUseCase = getVehicleByIdUseCase;
    }

    @Override
    public Optional<NotificationRecipient> findRecipient(UUID customerId) {
        try {
            GetCustomerResponse customer = getCustomerByIdUseCase.execute(customerId);
            return Optional.of(new NotificationRecipient(customer.name(), customer.email()));
        } catch (NotFoundException | DomainException e) {
            // Notificação é best-effort: um cadastro ausente não pode propagar erro para quem
            // disparou o evento. GetCustomerByIdUseCase lança NotFoundException, mas as duas
            // exceções são tratadas porque o módulo register usa ambas para "não encontrado".
            log.warn("Could not resolve recipient for customer {}: {}", customerId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findVehicleLabel(UUID vehicleId) {
        try {
            GetVehicleResponse vehicle = getVehicleByIdUseCase.execute(vehicleId);
            return Optional.of("%s %s (%s)".formatted(
                    vehicle.manufacturer(), vehicle.model(), vehicle.carLicensePlate()));
        } catch (NotFoundException | DomainException e) {
            log.warn("Could not resolve vehicle {}: {}", vehicleId, e.getMessage());
            return Optional.empty();
        }
    }
}
