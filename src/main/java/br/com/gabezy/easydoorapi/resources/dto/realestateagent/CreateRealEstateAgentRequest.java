package br.com.gabezy.easydoorapi.resources.dto.realestateagent;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRealEstateAgentRequest(
        @NotBlank
        String name,
        @NotBlank
        String cnpj,
        @NotBlank
        String creci,
        @NotBlank
        String phone,
        @NotBlank
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password,
        @NotNull
        @Valid
        AddressDTO address
) {
}
