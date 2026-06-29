package com.safiap.techchallengeoficinamecanica.modules.auth.application.service;

import java.util.UUID;

public interface CustomerRegistration {
    UUID register(String name, String email, String phone, String cnpjCpf);
}
