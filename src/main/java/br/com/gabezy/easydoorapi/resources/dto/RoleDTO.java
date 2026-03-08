package br.com.gabezy.easydoorapi.resources.dto;

import java.util.Set;

public record RoleDTO(
        Long id,
        String name,
        String description,
        Set<String> permissionCodes
) {}

