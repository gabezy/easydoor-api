package br.com.gabezy.easydoorapi.domain.role.repository;

import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface RoleRepository extends PanacheRepository<Role> {

    Optional<Role> findByName(String name);

    Optional<Role> findByNameWithPermissions(String name);

}
