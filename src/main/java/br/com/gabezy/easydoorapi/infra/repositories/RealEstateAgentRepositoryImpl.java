package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.domain.user.repositories.RealEstateAgentRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class RealEstateAgentRepositoryImpl implements RealEstateAgentRepository {

    @Override
    public Optional<RealEstateAgent> findByUserId(Long userId) {
        return find("user.id = ?1", userId).singleResultOptional();
    }
}
