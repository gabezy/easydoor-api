package br.com.gabezy.easydoorapi.resources.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

@Schema(name = "Role", description = "Role with its assigned permissions")
public record RoleDTO(
        @Schema(description = "Role identifier", example = "1")
        Long id,
        @Schema(description = "Role name", example = "ADMIN")
        String name,
        @Schema(description = "Role description", example = "Administrator with full access")
        String description,
        @Schema(description = "Permission codes assigned to the role")
        Set<String> permissionCodes
) {}
