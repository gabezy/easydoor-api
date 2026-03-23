package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Objects;

public record RegisterRequestDTO(
        @NotEmpty
        String username,
        @NotNull
        @Email
        String email,
        @NotEmpty
        String password,
        @NotBlank
        @CPF
        String cpf,
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

