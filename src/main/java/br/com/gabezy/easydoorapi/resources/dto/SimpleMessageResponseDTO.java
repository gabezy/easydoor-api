package br.com.gabezy.easydoorapi.resources.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SimpleMessageResponse", description = "Simple success response payload")
public record SimpleMessageResponseDTO(
        @Schema(description = "Success message", example = "Role assigned successfully")
        String message
) {
}
