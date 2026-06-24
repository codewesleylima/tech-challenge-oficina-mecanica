package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.commands.customer.RegisterCustomerCommand;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.RegisterCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;

    public RegisterCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public RegisterCustomerResponse execute(RegisterCustomerCommand request) {

        Customer customer = Customer.createCustomer(
                request.name(),
                new Email(request.email()),
                new Phone(request.phone()),
                new CPF(request.cpf())
        );

        customerRepository.findByCpf(customer.getCpf()).ifPresent(customerObj -> {
            throw new DomainException("already exists CPF "+customerObj.getCpf());
        });

        customerRepository.save(customer);

        return new RegisterCustomerResponse(customer.getName());

    }

}
