package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Objects;

@Schema(name = "RegisterClientRequest", description = "Payload used to register a client account")
public record RegisterRequestDTO(
        @Schema(description = "Unique username", example = "joao.silva")
        @NotEmpty
        String username,
        @Schema(description = "Unique e-mail address", example = "joao.silva@email.com")
        @NotNull
        @Email
        String email,
        @Schema(description = "Plain password", example = "StrongPassword@123")
        @NotEmpty
        String password,
        @Schema(description = "Client CPF", example = "12345678909")
        @NotBlank
        @CPF
        String cpf,
        @Schema(description = "Client full name", example = "Joao Silva")
        @NotBlank
        String name
) {
        public RegisterRequestDTO {
                if (Objects.isNull(password) || password.isBlank()) {
                        throw new IllegalArgumentException("Password is required");
                }
                if (Objects.isNull(email) || email.isBlank()) {
                        throw new IllegalArgumentException("Email is required");
                }
                if (Objects.isNull(username) || username.isBlank()) {
                        throw new IllegalArgumentException("Username is required");
                }
                if (Objects.isNull(cpf) || cpf.isBlank()) {
                        throw new IllegalArgumentException("CPF is required");
                }
                if (Objects.isNull(name) || name.isBlank()) {
                        throw new IllegalArgumentException("Name is required");
                }
        }
}
