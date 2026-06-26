package com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO;

import java.time.LocalDateTime;

public record RegistraTempoServicoDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        String notes
) {}
