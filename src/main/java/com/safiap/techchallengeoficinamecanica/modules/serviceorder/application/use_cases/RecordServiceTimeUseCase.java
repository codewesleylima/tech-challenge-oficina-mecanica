package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.RecordServiceTimeCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceTimeRecordResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceTimeRecord;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceTimeRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordServiceTimeUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceTimeRepository serviceTimeRepository;

    public RecordServiceTimeUseCase(ServiceOrderRepository serviceOrderRepository,
                                    ServiceTimeRepository serviceTimeRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceTimeRepository = serviceTimeRepository;
    }

    @Transactional
    public ServiceTimeRecordResponse execute(RecordServiceTimeCommand command) {
        serviceOrderRepository.findById(command.serviceOrderId())
                .filter(os -> os.getStatus() == ServiceOrderStatus.IN_EXECUTION)
                .orElseThrow(() -> new NotFoundException("Service order not found or not in IN_EXECUTION status: " + command.serviceOrderId()));

        if (command.endTime().isBefore(command.startTime()))
            throw new ConflictException("End time cannot be before start time");

        ServiceTimeRecord record = ServiceTimeRecord.create(
                command.serviceOrderId(), command.startTime(), command.endTime(), command.notes());
        serviceTimeRepository.save(record);

        return ServiceTimeRecordResponse.from(record);
    }
}
