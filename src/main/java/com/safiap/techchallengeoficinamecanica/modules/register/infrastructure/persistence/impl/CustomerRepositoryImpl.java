package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.impl;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mapper.CustomerMapper;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public void save(Customer customer) {
        customerJpaRepository.save(CustomerMapper.toEntity(customer));
    }

    @Override
    public Optional<Customer> findByCustomerId(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(CustomerMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByCpf(CPF cpf) {
        return customerJpaRepository.findByCpfEntity(cpf.cpf()).map(CustomerMapper::toDomain);
    }
}
