package br.com.gabezy.easydoorapi.resources.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TokenResponse", description = "Authentication tokens returned after login or registration")
public record TokenResponseDTO (
        @Schema(description = "JWT access token", example = "eyJhbGciOiJSUzI1NiJ9.access")
        String accessToken,
        @Schema(description = "Refresh token", example = "eyJhbGciOiJSUzI1NiJ9.refresh")
        String refreshToken,
        @Schema(description = "Access token expiration time in seconds", example = "3600")
        int expiresIn,
        @Schema(description = "Token type", example = "Bearer")
        String tokenType
) {
    public TokenResponseDTO(String accessToken, String refreshToken, int expiresIn) {
        this(accessToken, refreshToken, expiresIn, "Bearer");
    }
}
