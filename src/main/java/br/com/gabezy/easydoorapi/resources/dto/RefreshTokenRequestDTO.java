package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "RefreshTokenRequest", description = "Payload used to refresh or revoke authentication tokens")
public record RefreshTokenRequestDTO(
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9.refresh")
        @NotEmpty
        String refreshToken
) {
    public RefreshTokenRequestDTO {
        if (Objects.isNull(refreshToken) || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
    }
}
