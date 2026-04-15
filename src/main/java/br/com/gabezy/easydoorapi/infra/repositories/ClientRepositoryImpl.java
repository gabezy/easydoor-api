package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.repositories.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public Optional<Client> findByUserId(Long userId) {
       return find("user.id = ?1", userId).singleResultOptional();
    }
}
