package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for Role entities using Panache
 */
@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    /**
     * Find role by name
     *
     * @param name the role name to search for
     * @return Role if found, null otherwise
     */
    public Role findByName(String name) {
        return find("name", name).firstResult();
    }

    /**
     * Find role by name with permissions eagerly loaded
     *
     * @param name the role name
     * @return Role with permissions, null otherwise
     */
    public Role findByNameWithPermissions(String name) {
        return find("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = ?1", name)
                .firstResult();
    }
}

