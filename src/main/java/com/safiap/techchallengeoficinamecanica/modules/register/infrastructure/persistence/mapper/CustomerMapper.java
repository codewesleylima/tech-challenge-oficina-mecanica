package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.mapper;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.entities.Customer;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity.CustomerEntity;

public class CustomerMapper {

    public static CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.getCustomerId(),
                customer.getName(),
                customer.getCpf().cpf(),
                customer.getEmail().value(),
                customer.getPhone().value()
        );
    }

    public static Customer toDomain(CustomerEntity entity) {
        return Customer.buildCustomer(
                entity.getCustomerIdEntity(),
                entity.getNameEntity(),
                new Email(entity.getEmailEntity()),
                new Phone(entity.getPhoneEntity()),
                new CPF(entity.getCpfEntity())
        );
    }
}
