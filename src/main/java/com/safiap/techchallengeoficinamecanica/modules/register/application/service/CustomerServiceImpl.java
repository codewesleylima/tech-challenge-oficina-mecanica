package com.safiap.techchallengeoficinamecanica.modules.register.application.service;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerResponseDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public RegisterCustomerResponseDTO register(RegisterCustomerDTO request) {
        CPF cpf = new CPF(request.getCpfDTO());

        customerRepository.findByCpf(cpf).ifPresent(existing -> {
            throw new DomainException("Customer already registered with this CPF");
        });

        Customer customer = Customer.createCustomer(
                request.getNameDTO(),
                new Email(request.getEmailDTO()),
                new Phone(request.getPhoneDTO()),
                cpf
        );

        customerRepository.save(customer);

        return new RegisterCustomerResponseDTO(customer.getName());
    }
}
