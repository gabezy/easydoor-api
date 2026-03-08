package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.auth.services.RefreshTokenService;
import br.com.gabezy.easydoorapi.domain.auth.services.TokenGenerationService;
import br.com.gabezy.easydoorapi.domain.shared.Email;
import br.com.gabezy.easydoorapi.domain.user.entities.Role;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import br.com.gabezy.easydoorapi.infra.config.JwtProperties;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepository;
import br.com.gabezy.easydoorapi.infra.repositories.UserRepository;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenGenerationService tokenGenerationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       TokenGenerationService tokenGenerationService, RefreshTokenService refreshTokenService,
                       JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenGenerationService = tokenGenerationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public TokenResponseDTO register(RegisterRequestDTO request) {
        Email emailVO = new Email(request.email());
        if (Objects.nonNull(userRepository.findByEmail(emailVO.value()))) {
            throw new IllegalArgumentException("Email already exists");
        }

        String hashedPassword = BcryptUtil.bcryptHash(request.password());
        User user = new User(request.username(), emailVO.value(), hashedPassword);

        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            user.addRole(userRole);
        }

        userRepository.persist(user);

        return new TokenResponseDTO(
                tokenGenerationService.generateAccessToken(user).value(),
                tokenGenerationService.generateRefreshToken(user).value(),
                jwtProperties.accessToken().ttlSeconds()
        );
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsernameWithRoles(request.username());
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        if (!BcryptUtil.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.recordLogin();
        userRepository.persist(user);

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
        User user = userRepository.findByIdWithRoles(refreshToken.getUserId());

        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException("User not found or inactive");
        }

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



