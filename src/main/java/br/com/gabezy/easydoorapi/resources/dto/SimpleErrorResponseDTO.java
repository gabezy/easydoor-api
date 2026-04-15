package br.com.gabezy.easydoorapi.resources.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SimpleErrorResponse", description = "Simple error payload used by endpoints that return a short error message")
public record SimpleErrorResponseDTO(
        @Schema(description = "Error message", example = "User not found")
        String error
) {
}
