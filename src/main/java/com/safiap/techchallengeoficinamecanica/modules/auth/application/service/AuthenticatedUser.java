package com.safiap.techchallengeoficinamecanica.modules.auth.application.service;

import java.util.List;

public record AuthenticatedUser(String subject, List<String> roles) {
}
