package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.auth.services.RefreshTokenService;
import br.com.gabezy.easydoorapi.domain.auth.services.TokenGenerationService;
import br.com.gabezy.easydoorapi.domain.shared.vo.Cpf;
import br.com.gabezy.easydoorapi.domain.shared.vo.Email;
import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.LoginRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RegisterRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.TokenResponseDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class AuthService {

    private final UserService userService;
    private final RoleRepositoryImpl roleRepositoryImpl;
    private final TokenGenerationService tokenGenerationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;

    public AuthService(UserService userService, RoleRepositoryImpl roleRepositoryImpl,
                       TokenGenerationService tokenGenerationService, RefreshTokenService refreshTokenService,
                       JwtProperties jwtProperties) {
        this.userService = userService;
        this.roleRepositoryImpl = roleRepositoryImpl;
        this.tokenGenerationService = tokenGenerationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public TokenResponseDTO register(RegisterRequestDTO request) {
        Email emailVO = new Email(request.email());
        if (userService.usernameExists(request.username()) || userService.emailExists(emailVO.value())) {
            throw new IllegalArgumentException("Username or email already exists");
        }
        String hashedPassword = BcryptUtil.bcryptHash(request.password());
        User user = new User(request.username(), emailVO.value(), hashedPassword);

        roleRepositoryImpl.findByName("USER")
                .ifPresent(user::addRole);

        user.persist();

        var client = new Client(
                request.username(),
                new Cpf(request.cpf()).value(),
                user.id
        );

        client.persist();

        return new TokenResponseDTO(
                tokenGenerationService.generateAccessToken(user).value(),
                tokenGenerationService.generateRefreshToken(user).value(),
                jwtProperties.accessToken().ttlSeconds()
        );
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        User user = userService.findUserWithRoles(request.username());

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        if (!BcryptUtil.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.recordLogin();
        PanacheEntityBase.persist(user);

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
        var user = userService.findUserWithRoles(refreshToken.getUserId())
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



