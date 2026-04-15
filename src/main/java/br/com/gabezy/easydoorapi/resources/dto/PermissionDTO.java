package br.com.gabezy.easydoorapi.resources.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Permission", description = "Permission available for assignment to roles")
public record PermissionDTO(
        @Schema(description = "Permission identifier", example = "1")
        Long id,
        @Schema(description = "Permission code", example = "VIEW_USERS")
        String code,
        @Schema(description = "Permission description", example = "Read user information")
        String description
) {}
