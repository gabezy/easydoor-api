package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.auth.services.RefreshTokenService;
import br.com.gabezy.easydoorapi.domain.auth.services.TokenGenerationService;
import br.com.gabezy.easydoorapi.domain.shared.vo.Email;
import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.LoginRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RegisterRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.TokenResponseDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class AuthService {

    private final UserRepositoryImpl userRepositoryImpl;
    private final RoleRepositoryImpl roleRepositoryImpl;
    private final TokenGenerationService tokenGenerationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepositoryImpl userRepositoryImpl, RoleRepositoryImpl roleRepositoryImpl,
                       TokenGenerationService tokenGenerationService, RefreshTokenService refreshTokenService,
                       JwtProperties jwtProperties) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.roleRepositoryImpl = roleRepositoryImpl;
        this.tokenGenerationService = tokenGenerationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public TokenResponseDTO register(RegisterRequestDTO request) {
        Email emailVO = new Email(request.email());
        if (Objects.nonNull(userRepositoryImpl.findByEmail(emailVO.value()))) {
            throw new IllegalArgumentException("Email already exists");
        }

        String hashedPassword = BcryptUtil.bcryptHash(request.password());
        User user = new User(request.username(), emailVO.value(), hashedPassword);

        roleRepositoryImpl.findByName("USER")
                .ifPresent(user::addRole);

        userRepositoryImpl.persist(user);

        return new TokenResponseDTO(
                tokenGenerationService.generateAccessToken(user).value(),
                tokenGenerationService.generateRefreshToken(user).value(),
                jwtProperties.accessToken().ttlSeconds()
        );
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        User user = userRepositoryImpl.findByUsernameWithRoles(request.username())
                .orElseThrow();

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        if (!BcryptUtil.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.recordLogin();
        userRepositoryImpl.persist(user);

        String accessToken = tokenGenerationService.generateAccessToken(user).value();
        String refreshToken = tokenGenerationService.generateRefreshToken(user).value();

        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.refreshToken().ttlSeconds());
        refreshTokenService.createToken(refreshToken, user.id, expiresAt);

        return new TokenResponseDTO(accessToken, refreshToken, jwtProperties.accessToken().ttlSeconds());
    }

    @Transactional
    public TokenResponseDTO refreshToken(String refreshTokenValue) {
        if (refreshTokenValue == null || refreshTokenValue.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        if (!refreshTokenService.isTokenValid(refreshTokenValue)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        var refreshToken = refreshTokenService.getValidToken(refreshTokenValue);
        var user = userRepositoryImpl.findByIdWithRoles(refreshToken.getUserId())
                .filter(User::isActive)
                .orElseThrow();

        String newAccessToken = tokenGenerationService.generateAccessToken(user).value();

        return new TokenResponseDTO(
                newAccessToken,
                refreshTokenValue,
                jwtProperties.accessToken().ttlSeconds()
        );
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        if (refreshTokenValue != null && !refreshTokenValue.isEmpty()) {
            refreshTokenService.revokeToken(refreshTokenValue);
        }
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenService.revokeAllUserTokens(userId);
    }

}



