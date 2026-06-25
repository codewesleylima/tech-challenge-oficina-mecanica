package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.AlterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.AlterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class AlterCustomerUseCase {

    private final CustomerRepository customerRepository;

    public AlterCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public AlterCustomerResponse execute(AlterCustomerCommand command){

        Customer customer = customerRepository.findByCustomerId(command.id())
                .orElseThrow( () -> new DomainException("Customer not found."));

        customer.alterCustomer(
                command.Name(),
                new Email(command.Email()),
                new Phone(command.Phone()),
                new CPF(command.Cpf())
        );

        customerRepository.save(customer);

        return new AlterCustomerResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail().value(),
                customer.getPhone().value(),
                customer.getCpf().cpf());
    }

}
