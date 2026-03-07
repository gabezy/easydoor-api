package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.shared.Token;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenGenerationService {

    private final JwtProperties jwtProperties;

    public TokenGenerationService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    public Token generateAccessToken(User user) {
        if (!user.active) {
            throw new IllegalStateException("Cannot generate token for inactive user");
        }

        try {
            String token = Jwt.issuer("easydoor-api")
                    .subject(user.username)
                    .audience("easydoor-users")
                    .claim("email", user.email)
                    .claim("user_id", user.id)
                    .claim("roles", user.roles.stream()
                            .map(r -> r.name)
                            .collect(Collectors.toSet()))
                    .claim("permissions", user.roles.stream()
                            .flatMap(r -> r.permissions.stream())
                            .map(p -> p.code)
                            .collect(Collectors.toSet()))
                    .expiresIn(Duration.ofSeconds(jwtProperties.accessToken().ttlSeconds()))
                    .sign();

            return new Token(token);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate access token", e);
        }
    }

    public Token generateRefreshToken(User user) {
        if (!user.active) {
            throw new IllegalStateException("Cannot generate token for inactive user");
        }

        try {
            String token = Jwt.issuer("easydoor-api")
                    .subject(user.username)
                    .audience("easydoor-users")
                    .claim("user_id", user.id)
                    .claim("type", "refresh")
                    .expiresIn(Duration.ofSeconds(jwtProperties.refreshToken().ttlSeconds()))
                    .sign();

            return new Token(token);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }
}
