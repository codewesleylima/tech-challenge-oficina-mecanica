package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.repositories.CustomerRepository;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities.JPACustomerEntity;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mappers.CustomerMapper;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repositories.JPACustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryImp implements CustomerRepository {

    private final JPACustomerRepository jpacustomerRepository;

    public CustomerRepositoryImp(JPACustomerRepository jpacustomerRepository) {
        this.jpacustomerRepository = jpacustomerRepository;
    }

    @Override
    public void save(Customer customer) {
        jpacustomerRepository.save(CustomerMapper.toJPA(customer));
    }

    @Override
    public Optional<Customer> findByCustomerId(UUID customerId) {
        return jpacustomerRepository
                .findById(customerId)
                .map(CustomerMapper::toEntity);
    }

    @Override
    public Optional<Customer> findByCpf(CPF cpf) {
        return jpacustomerRepository
                .findByCpf(cpf.cpf())
                .map(CustomerMapper::toEntity);
    }

    @Override
    public void delete(Customer customer) {
        jpacustomerRepository.delete(CustomerMapper.toJPA(customer));
    }

    @Override
    public List<Customer> findAll() {
        return jpacustomerRepository.findAll().stream().map(CustomerMapper::toEntity).toList();
    }
}
