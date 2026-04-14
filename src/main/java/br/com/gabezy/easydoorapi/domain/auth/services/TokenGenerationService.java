package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.shared.vo.Token;
import br.com.gabezy.easydoorapi.domain.role.entities.Permission;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        boolean isAdmin = false;

        for (var role : user.getRoles()) {
            if (role.getName().equalsIgnoreCase("ADMIN")) {
                isAdmin = true;
            }

            roles.add(role.getName());
            permissions.addAll(role.getPermissions().stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toSet()));
        }

        if (isAdmin) {
            permissions.add("ADMIN");
        }

        String token = Jwt.issuer("easydoor-api")
                .subject(user.getUsername())
                .audience("easydoor-users")
                .groups(permissions)
                .claim("roles", roles)
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
