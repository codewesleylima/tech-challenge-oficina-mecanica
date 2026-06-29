package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.AlterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.AlterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CnpjCpf;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlterCustomerUseCase {

    private final CustomerRepository customerRepository;

    public AlterCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public AlterCustomerResponse execute(AlterCustomerCommand command){

        Customer customer = customerRepository.findByCustomerId(command.id())
                .orElseThrow( () -> new NotFoundException("Customer not found."));

        customer.alterCustomer(
                command.name(),
                new Email(command.email()),
                new Phone(command.phone()),
                new CnpjCpf(command.cnpjCpf())
        );

        customerRepository.save(customer);

        return new AlterCustomerResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail().value(),
                customer.getPhone().value(),
                customer.getCnpjCpf().value());
    }

}
