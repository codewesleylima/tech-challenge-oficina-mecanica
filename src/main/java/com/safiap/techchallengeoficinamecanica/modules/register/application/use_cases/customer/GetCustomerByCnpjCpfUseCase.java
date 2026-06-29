package com.safiap.techchallengeoficinamecanica.modules.register.application.use_cases.customer;

import com.safiap.techchallengeoficinamecanica.modules.register.application.responses.customer.GetCustomerResponse;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CnpjCpf;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerByCnpjCpfUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerByCnpjCpfUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public GetCustomerResponse execute(String cnpjCpf){

        CnpjCpf cnpjCpfObject = new CnpjCpf(cnpjCpf);

        return customerRepository
                .findByCnpjCpf(cnpjCpfObject)
                .map(customer ->
                        new GetCustomerResponse(
                                customer.getCustomerId(),
                                customer.getName(),
                                customer.getEmail().value(),
                                customer.getPhone().value(),
                                customer.getCnpjCpf().value()
                        )
                ).orElseThrow(
                        () -> new NotFoundException("Customer with document " + cnpjCpf + " not found.")
                );
    }

}
