package com.safiap.techchallengeoficinamecanica.modules.register.application.service;

import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleDTO;
import com.safiap.techchallengeoficinamecanica.modules.register.application.dto.AddVehicleResponseDTO;

public interface VehicleService {

    AddVehicleResponseDTO add(AddVehicleDTO request);
}
