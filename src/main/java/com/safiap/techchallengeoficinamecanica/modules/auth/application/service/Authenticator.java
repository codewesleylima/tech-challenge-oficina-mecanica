package com.safiap.techchallengeoficinamecanica.modules.auth.application.service;

public interface Authenticator {

    AuthenticatedUser authenticate(String email, String rawPassword);
}
