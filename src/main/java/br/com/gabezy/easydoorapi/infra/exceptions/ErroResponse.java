package br.com.gabezy.easydoorapi.infra.exceptions;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Standard error payload returned by the API")
public record ErroResponse(
        @Schema(description = "Error code", example = "Bad Request")
        String error,
        @Schema(description = "Human-readable error message", example = "Email already exists")
        String message,
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "Error timestamp in milliseconds since epoch", example = "1776212000000")
        long timestamp
) {
    public ErroResponse(String error, String message, int status) {
        this(error, message, status, System.currentTimeMillis());
    }
}
