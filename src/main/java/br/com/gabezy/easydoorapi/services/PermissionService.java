package br.com.gabezy.easydoorapi.services;

import br.com.gabezy.easydoorapi.domain.user.entities.Permission;
import br.com.gabezy.easydoorapi.domain.user.entities.Role;
import br.com.gabezy.easydoorapi.infra.repositories.PermissionRepository;
import br.com.gabezy.easydoorapi.infra.repositories.RoleRepository;
import br.com.gabezy.easydoorapi.resources.dto.PermissionDTO;
import br.com.gabezy.easydoorapi.resources.dto.RoleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.listAll().stream()
                .map(this::mapPermissionToDTO)
                .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId);
        return permission != null ? mapPermissionToDTO(permission) : null;
    }

    @Transactional
    public PermissionDTO createPermission(String code, String description) {
        if (permissionRepository.findByCode(code) != null) {
            throw new IllegalArgumentException("Permission with code '" + code + "' already exists");
        }

        Permission permission = new Permission(code, description);
        permissionRepository.persist(permission);
        return mapPermissionToDTO(permission);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.listAll().stream()
                .map(this::mapRoleToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId);
        return role != null ? mapRoleToDTO(role) : null;
    }

    public RoleDTO getRoleByName(String roleName) {
        Role role = roleRepository.findByNameWithPermissions(roleName);
        return role != null ? mapRoleToDTO(role) : null;
    }

    @Transactional
    public RoleDTO createRole(String name, String description) {
        if (roleRepository.findByName(name) != null) {
            throw new IllegalArgumentException("Role with name '" + name + "' already exists");
        }

        Role role = new Role(name, description);
        roleRepository.persist(role);
        return mapRoleToDTO(role);
    }

    @Transactional
    public void addPermissionToRole(Long roleId, String permissionCode) {
        Role role = roleRepository.findById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        Permission permission = permissionRepository.findByCode(permissionCode);
        if (permission == null) {
            throw new IllegalArgumentException("Permission not found");
        }

        role.addPermission(permission);
        roleRepository.persist(role);
    }

    @Transactional
    public void removePermissionFromRole(Long roleId, String permissionCode) {
        Role role = roleRepository.findById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        Permission permission = permissionRepository.findByCode(permissionCode);
        if (permission == null) {
            throw new IllegalArgumentException("Permission not found");
        }

        role.removePermission(permission);
        roleRepository.persist(role);
    }

    private PermissionDTO mapPermissionToDTO(Permission permission) {
        return new PermissionDTO(permission.id, permission.code, permission.description);
    }

    private RoleDTO mapRoleToDTO(Role role) {
        return new RoleDTO(
                role.id,
                role.name,
                role.description,
                role.permissions.stream().map(p -> p.code).collect(Collectors.toSet())
        );
    }
}

