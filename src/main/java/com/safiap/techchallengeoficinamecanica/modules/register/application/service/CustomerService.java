package com.safiap.techchallengeoficinamecanica.modules.register.application.service;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.RegisterCustomerResponseDTO;

public interface CustomerService {

    RegisterCustomerResponseDTO register(RegisterCustomerDTO request);
}
