package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.shared.Token;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        var groups = user.roles.stream()
                .flatMap(role -> role.permissions.stream())
                .map(permission -> permission.code)
                .collect(Collectors.toSet());

        String token = Jwt.issuer("easydoor-api")
                .subject(user.username)
                .audience("easydoor-users")
                .groups(groups)
                .claim("email", user.email)
                .claim("user_id", user.id)
                .expiresIn(Duration.ofSeconds(jwtProperties.accessToken().ttlSeconds()))
                .sign();

        return new Token(token);
    }

    public Token generateRefreshToken(User user) {
        if (!user.active) {
            throw new IllegalStateException("Cannot generate token for inactive user");
        }

        String token = Jwt.issuer("easydoor-api")
                .subject(user.username)
                .audience("easydoor-users")
                .claim("user_id", user.id)
                .claim("type", "refresh")
                .expiresIn(Duration.ofSeconds(jwtProperties.refreshToken().ttlSeconds()))
                .sign();

        return new Token(token);
    }
}
