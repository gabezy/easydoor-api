package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.user.entities.Permission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for Permission entities using Panache
 */
@ApplicationScoped
public class PermissionRepository implements PanacheRepository<Permission> {

    /**
     * Find permission by code
     *
     * @param code the permission code to search for
     * @return Permission if found, null otherwise
     */
    public Permission findByCode(String code) {
        return find("code", code).firstResult();
    }

    /**
     * Check if permission with code exists
     *
     * @param code the permission code
     * @return true if exists, false otherwise
     */
    public boolean existsByCode(String code) {
        return findByCode(code) != null;
    }
}

