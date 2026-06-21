package com.safiap.techchallengeoficinamecanica.modules.register.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVehicleDTO {
    private UUID customerIdDTO;
    private String carLicensePlateDTO;
    private String modelDTO;
    private String manufacturerDTO;
    private Integer kilometersDTO;
    private Integer yearDTO;
}
