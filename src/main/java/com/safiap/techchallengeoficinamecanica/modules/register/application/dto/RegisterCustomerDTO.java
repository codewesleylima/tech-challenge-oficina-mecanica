package com.safiap.techchallengeoficinamecanica.modules.register.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerDTO {
    private String nameDTO;
    private String emailDTO;
    private String phoneDTO;
    private String cpfDTO;
}
