package br.com.gabezy.easydoorapi.resources.dto.realestateagent;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(
        name = "CreateRealEstateAgentRequest",
        description = "Payload used to create a real estate agent together with its related user"
)
public record CreateRealEstateAgentRequest(
        @Schema(description = "Real estate agency or agent display name", example = "Imobiliaria Horizonte")
        @NotBlank
        String name,
        @Schema(description = "CNPJ identifier", example = "12345678000199")
        @NotBlank
        String cnpj,
        @Schema(description = "CRECI registration", example = "123456")
        @NotBlank
        String creci,
        @Schema(description = "Contact phone number", example = "11999999999")
        @NotBlank
        String phone,
        @Schema(description = "Unique login name for the related user", example = "horizonte.agent")
        @NotBlank
        String username,
        @Schema(description = "Unique e-mail for the related user", example = "agent@horizonte.com")
        @NotBlank
        @Email
        String email,
        @Schema(description = "Plain password that will be stored hashed", example = "StrongPassword@123")
        @NotBlank
        String password,
        @Schema(description = "Address data for the real estate agent")
        @NotNull
        @Valid
        AddressDTO address
) {
}
