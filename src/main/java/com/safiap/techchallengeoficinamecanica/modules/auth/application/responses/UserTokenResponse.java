package com.safiap.techchallengeoficinamecanica.modules.auth.application.responses;

public record UserTokenResponse(String accessToken, long expiresAt, String tokenType) {
}
