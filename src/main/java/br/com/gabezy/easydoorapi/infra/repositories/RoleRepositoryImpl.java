package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import br.com.gabezy.easydoorapi.domain.role.repository.RoleRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class RoleRepositoryImpl implements RoleRepository {

    public Optional<Role> findByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public Optional<Role> findByNameWithPermissions(String name) {
        return find("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = ?1", name)
                .singleResultOptional();
    }
}

