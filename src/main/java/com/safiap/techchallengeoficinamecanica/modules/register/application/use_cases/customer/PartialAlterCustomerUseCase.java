package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.PartialAlterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.PartialAlterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class PartialAlterCustomerUseCase {
    private final CustomerRepository customerRepository;

    public PartialAlterCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public PartialAlterCustomerResponse execute(PartialAlterCustomerCommand command){

        Customer customer = customerRepository.findByCustomerId(command.id())
                .orElseThrow( () -> new DomainException("Customer not found."));

        customer.partialUpdateCustomer(
                command.Name(),
                command.Email(),
                command.Phone(),
                command.Cpf()
        );

        customerRepository.save(customer);

        return new PartialAlterCustomerResponse(
                customer.getName(),
                customer.getEmail().value(),
                customer.getPhone().value(),
                customer.getCpf().cpf());
    }
}
