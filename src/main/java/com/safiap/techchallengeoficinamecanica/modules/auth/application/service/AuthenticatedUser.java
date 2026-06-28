package com.safiap.techchallengeoficinamecanica.modules.auth.application.service;

import java.util.List;
import java.util.UUID;

public record AuthenticatedUser(String subject, List<String> roles, UUID customerId) {
}
