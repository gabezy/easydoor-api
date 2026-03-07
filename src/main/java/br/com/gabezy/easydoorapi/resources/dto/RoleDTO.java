package br.com.gabezy.easydoorapi.resources.dto;

import java.util.Set;

/**
 * Role DTO for REST responses
 */
public class RoleDTO {
    public Long id;
    public String name;
    public String description;
    public Set<String> permissionCodes;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String name, String description, Set<String> permissionCodes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissionCodes = permissionCodes;
    }
}

