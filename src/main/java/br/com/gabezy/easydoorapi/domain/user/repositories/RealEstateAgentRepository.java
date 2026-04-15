package br.com.gabezy.easydoorapi.domain.user.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface RealEstateAgentRepository extends PanacheRepository<RealEstateAgent> {

    Optional<RealEstateAgent> findByUserId(Long userId);

}
