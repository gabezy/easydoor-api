package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "LoginRequest", description = "Credentials used to authenticate a user")
public record LoginRequestDTO(
        @Schema(description = "Username", example = "admin")
        @NotEmpty
        String username,
        @Schema(description = "Password", example = "admin123")
        @NotEmpty
        String password
) {}
