package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.auth.entities.RefreshToken;
import br.com.gabezy.easydoorapi.domain.auth.repositories.RefreshTokenRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    public Optional<RefreshToken> findByToken(String token) {
        return find("token", token).singleResultOptional();
    }

    public List<RefreshToken> findValidByUserId(Long userId) {
        return find("userId = ?1 AND revokedAt IS NULL", userId).list();
    }

    public void revokeAllByUserId(Long userId) {
        update("UPDATE RefreshToken SET revokedAt = NOW() WHERE userId = ?1 AND revokedAt IS NULL", userId);
    }

}

