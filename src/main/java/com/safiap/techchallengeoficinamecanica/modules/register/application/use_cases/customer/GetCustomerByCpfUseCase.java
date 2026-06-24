package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public class GetCustomerByCpfUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerByCpfUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public GetCustomerResponse execute(String cpf){

        CPF cpfObject = new CPF(cpf);

        return customerRepository
                .findByCpf(cpfObject)
                .map(customer ->
                        new GetCustomerResponse(
                                customer.getCustomerId(),
                                customer.getName(),
                                customer.getEmail().value(),
                                customer.getPhone().value(),
                                customer.getCpf().cpf()
                        )
                ).orElseThrow(
                        () -> new DomainException("Customer with cpf " + cpf + " not found.")
                );
    }

}
