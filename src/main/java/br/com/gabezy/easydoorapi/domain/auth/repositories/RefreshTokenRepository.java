package br.com.gabezy.easydoorapi.domain.auth.repositories;

import br.com.gabezy.easydoorapi.domain.auth.entities.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends PanacheRepository<RefreshToken> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken > findValidByUserId(Long userId);

    void revokeAllByUserId(Long userId);


}
