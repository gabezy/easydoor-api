package br.com.gabezy.easydoorapi.infra.repositories;

import br.com.gabezy.easydoorapi.domain.role.entities.Permission;
import br.com.gabezy.easydoorapi.domain.role.repository.PermissionRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PermissionRepositoryImpl implements PermissionRepository {

    @Override
    public Optional<Permission> findByCode(String code) {
        return find("code", code).singleResultOptional();
    }
}

