package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.security;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.AuthenticatedUser;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.TokenService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService implements TokenService {

    private final JwtEncoder encoder;

    public JwtTokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public UserTokenResponse generateToken(AuthenticatedUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(1, ChronoUnit.HOURS);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.subject())
                .claim("roles", user.roles());

        if (user.customerId() != null) {
            claimsBuilder.claim("customerId", user.customerId().toString());
        }

        JwtClaimsSet claims = claimsBuilder.build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return new UserTokenResponse(
                encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue(),
                expiresAt.toEpochMilli(),
                "Bearer"
        );
    }
}
