package br.com.gabezy.easydoorapi.domain.auth.services;

import br.com.gabezy.easydoorapi.domain.auth.entities.RefreshToken;
import br.com.gabezy.easydoorapi.infra.repositories.RefreshTokenRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class RefreshTokenService {

    private final RefreshTokenRepositoryImpl refreshTokenRepositoryImpl;

    public RefreshTokenService(RefreshTokenRepositoryImpl refreshTokenRepositoryImpl) {
        this.refreshTokenRepositoryImpl = refreshTokenRepositoryImpl;
    }

    public boolean isTokenValid(String tokenValue) {
         return refreshTokenRepositoryImpl.findByToken(tokenValue)
                 .map(RefreshToken::isValid)
                 .orElse(false);
    }

    public RefreshToken getValidToken(String tokenValue) {
        return refreshTokenRepositoryImpl.findByToken(tokenValue)
                .filter(RefreshToken::isValid)
                .orElse(null);
    }

    public void revokeToken(String tokenValue) {
        refreshTokenRepositoryImpl.findByToken(tokenValue)
                .ifPresent(refreshToken -> {
                    refreshToken.revoke();
                    refreshTokenRepositoryImpl.persist(refreshToken);
                });
    }

    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepositoryImpl.revokeAllByUserId(userId);
    }

    public void createToken(String tokenValue, Long userId, LocalDateTime expiresAt) {
        RefreshToken token = new RefreshToken(tokenValue, userId, expiresAt);
        refreshTokenRepositoryImpl.persist(token);
    }

}

