package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.role.entities.Permission;
import br.com.gabezy.easydoorapi.domain.role.entities.Role;
import br.com.gabezy.easydoorapi.infra.repositories.PermissionRepositoryImpl;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepositoryImpl;
import br.com.gabezy.easydoorapi.resources.dto.PermissionDTO;
import br.com.gabezy.easydoorapi.resources.dto.RoleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleService {

    private final PermissionRepositoryImpl permissionRepositoryImpl;
    private final RoleRepositoryImpl roleRepositoryImpl;

    public RoleService(PermissionRepositoryImpl permissionRepositoryImpl, RoleRepositoryImpl roleRepositoryImpl) {
        this.permissionRepositoryImpl = permissionRepositoryImpl;
        this.roleRepositoryImpl = roleRepositoryImpl;
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepositoryImpl.listAll().stream()
                .map(this::mapPermissionToDTO)
                .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(Long permissionId) {
        Permission permission = permissionRepositoryImpl.findById(permissionId);
        return permission != null ? mapPermissionToDTO(permission) : null;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepositoryImpl.listAll().stream()
                .map(this::mapRoleToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepositoryImpl.findById(roleId);
        return role != null ? mapRoleToDTO(role) : null;
    }

    @Transactional
    public RoleDTO createRole(String name, String description) {
        if (roleRepositoryImpl.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Role with name '" + name + "' already exists");
        }

        Role role = new Role(name, description);
        roleRepositoryImpl.persist(role);
        return mapRoleToDTO(role);
    }

    @Transactional
    public void addPermissionToRole(Long roleId, String permissionCode) {
        Role role = findRole(roleId);

        Permission permission = findPermission(permissionCode);
        role.addPermission(permission);
        roleRepositoryImpl.persist(role);
    }

    @Transactional
    public void removePermissionFromRole(Long roleId, String permissionCode) {
        var role = findRole(roleId);
        Permission permission = findPermission(permissionCode);
        role.removePermission(permission);
        roleRepositoryImpl.persist(role);
    }

    private Permission findPermission(String code) {
        return permissionRepositoryImpl.findByCode(code)
                .orElseThrow();
    }

    private Role findRole(Long id) {
        return roleRepositoryImpl.findByIdOptional(id)
                .orElseThrow();
    }

    private PermissionDTO mapPermissionToDTO(Permission permission) {
        return new PermissionDTO(permission.id, permission.getCode(), permission.getDescription());
    }

    private RoleDTO mapRoleToDTO(Role role) {
        return new RoleDTO(
                role.id,
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toSet())
        );
    }
}

