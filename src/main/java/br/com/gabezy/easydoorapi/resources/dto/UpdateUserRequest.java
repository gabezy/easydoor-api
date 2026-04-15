package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "UpdateUserRequest", description = "Payload used to update a user")
public record UpdateUserRequest(
        @Schema(description = "New e-mail address", example = "updated.user@easydoor.com")
        @Email
        @NotEmpty
        String email,
        @Schema(description = "Whether the user is active", example = "true")
        boolean active
) {
        public UpdateUserRequest {
                if (Objects.isNull(email) || email.isBlank()) {
                        throw new IllegalArgumentException("Email is required");
                }
        }
}
