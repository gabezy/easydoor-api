package br.com.gabezy.easydoorapi.domain.role.repository;

import br.com.gabezy.easydoorapi.domain.role.entities.Permission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface PermissionRepository extends PanacheRepository<Permission> {

    Optional<Permission> findByCode(String code);

}
