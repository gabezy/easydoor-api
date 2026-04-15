package br.com.gabezy.easydoorapi.domain.user.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface ClientRepository extends PanacheRepository<Client> {

    Optional<Client> findByUserId(Long userId);

}
