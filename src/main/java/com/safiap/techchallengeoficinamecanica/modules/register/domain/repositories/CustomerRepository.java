package com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CnpjCpf;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findByCustomerId(UUID customerId);
    Optional<Customer> findByCnpjCpf(CnpjCpf cnpjCpf);
    void delete(Customer customer);
    List<Customer> findAll();
}
