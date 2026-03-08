package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotEmpty
        String username,
        @NotEmpty
        String password
) {}

