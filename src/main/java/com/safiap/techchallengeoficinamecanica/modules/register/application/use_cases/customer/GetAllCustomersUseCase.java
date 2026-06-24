package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;

import java.util.List;

public class GetAllCustomersUseCase {

    private final CustomerRepository customerRepository;

    public GetAllCustomersUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<GetCustomerResponse> execute(){
        return customerRepository
                .findAll()
                .stream()
                .map(customer ->
                        new GetCustomerResponse(
                                customer.getCustomerId(),
                                customer.getName(),
                                customer.getEmail().value(),
                                customer.getPhone().value(),
                                customer.getCpf().cpf()
                        )
                )
                .toList();
    }


}
