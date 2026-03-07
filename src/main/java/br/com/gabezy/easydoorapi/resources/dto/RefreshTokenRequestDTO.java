package br.com.gabezy.easydoorapi.resources.dto;

/**
 * Refresh token request DTO
 */
public class RefreshTokenRequestDTO {
    public String refreshToken;

    public RefreshTokenRequestDTO() {
    }

    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

