package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.auth.entities.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repository for RefreshToken entities using Panache
 */
@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshToken> {

    /**
     * Find refresh token by token string
     *
     * @param token the token value to search for
     * @return RefreshToken if found, null otherwise
     */
    public RefreshToken findByToken(String token) {
        return find("token", token).firstResult();
    }

    /**
     * Find all valid (not revoked) refresh tokens for a user
     *
     * @param userId the user ID
     * @return List of valid RefreshToken entities
     */
    public List<RefreshToken> findValidByUserId(Long userId) {
        return find("userId = ?1 AND revokedAt IS NULL", userId).list();
    }

    /**
     * Revoke all refresh tokens for a user by setting revokedAt timestamp
     *
     * @param userId the user ID
     */
    public void revokeAllByUserId(Long userId) {
        update("UPDATE RefreshToken SET revokedAt = NOW() WHERE userId = ?1 AND revokedAt IS NULL", userId);
    }

    /**
     * Delete expired tokens (cleanup)
     *
     * @return number of rows deleted
     */
    public long deleteExpiredTokens() {
        return delete("expiresAt < NOW()");
    }
}

