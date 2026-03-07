package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.auth.entities.RefreshToken;
import br.com.gabezy.easydoorapi.domain.shared.UserId;
import br.com.gabezy.easydoorapi.infra.repositories.RefreshTokenRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean isTokenValid(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue);
        if (token == null) {
            return false;
        }
        return token.isValid();
    }

    public RefreshToken getValidToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue);
        if (token != null && token.isValid()) {
            return token;
        }
        return null;
    }

    public void revokeToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue);
        if (token != null) {
            token.revoke();
            refreshTokenRepository.persist(token);
        }
    }

    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }

    public RefreshToken createToken(String tokenValue, Long userId, LocalDateTime expiresAt) {
        RefreshToken token = new RefreshToken(tokenValue, userId, expiresAt);
        refreshTokenRepository.persist(token);
        return token;
    }
}

