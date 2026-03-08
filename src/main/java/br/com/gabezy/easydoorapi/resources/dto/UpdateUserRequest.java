package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public record UpdateUserRequest(
        @Email
        @NotEmpty
        String email,
        boolean active
) {
        public UpdateUserRequest {
                if (Objects.isNull(email) || email.isBlank()) {
                        throw new IllegalArgumentException("Email is required");
                }
        }
}
