package br.com.gabezy.easydoorapi.resources.dto;

public record TokenResponseDTO (
        String accessToken,
        String refreshToken,
        int expiresIn,
        String tokenType
) {
    public TokenResponseDTO(String accessToken, String refreshToken, int expiresIn) {
        this(accessToken, refreshToken, expiresIn, "Bearer");
    }
}

