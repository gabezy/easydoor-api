package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public record RefreshTokenRequestDTO(
        @NotEmpty
        String refreshToken
) {
    public RefreshTokenRequestDTO {
        if (Objects.isNull(refreshToken) || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
    }
}

