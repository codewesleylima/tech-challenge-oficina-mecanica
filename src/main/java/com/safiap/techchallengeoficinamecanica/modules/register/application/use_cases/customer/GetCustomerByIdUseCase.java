package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCustomerByIdUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public GetCustomerResponse execute(UUID id){
        return customerRepository
                .findByCustomerId(id)
                .map(customer ->
                        new GetCustomerResponse(
                                customer.getCustomerId(),
                                customer.getName(),
                                customer.getEmail().value(),
                                customer.getPhone().value(),
                                customer.getCpf().cpf()
                        )
                ).orElseThrow(
                        () -> new DomainException("Customer with id " + id + " not found.")
                );
    }

}
