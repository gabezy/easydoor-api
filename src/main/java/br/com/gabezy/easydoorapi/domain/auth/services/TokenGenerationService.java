package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.shared.Token;
import br.com.gabezy.easydoorapi.domain.user.entities.Permission;
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
        if (!user.isActive()) {
            throw new IllegalStateException("Cannot generate token for inactive user");
        }

        var groups = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        String token = Jwt.issuer("easydoor-api")
                .subject(user.getUsername())
                .audience("easydoor-users")
                .groups(groups)
                .claim("email", user.getEmail())
                .claim("user_id", user.id)
                .expiresIn(Duration.ofSeconds(jwtProperties.accessToken().ttlSeconds()))
                .sign();

        return new Token(token);
    }

    public Token generateRefreshToken(User user) {
        if (!user.isActive()) {
            throw new IllegalStateException("Cannot generate token for inactive user");
        }

        String token = Jwt.issuer("easydoor-api")
                .subject(user.getUsername())
                .audience("easydoor-users")
                .claim("user_id", user.id)
                .claim("type", "refresh")
                .expiresIn(Duration.ofSeconds(jwtProperties.refreshToken().ttlSeconds()))
                .sign();

        return new Token(token);
    }
}
