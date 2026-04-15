package br.com.gabezy.easydoorapi.resources.dto.realestateagent;

import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "RealEstateAgent", description = "Detailed representation of a real estate agent and its related user")
public record RealEstateAgentDTO(
        @Schema(description = "Real estate agent identifier", example = "20")
        Long id,
        @Schema(description = "Real estate agency or agent display name", example = "Imobiliaria Horizonte")
        String name,
        @Schema(description = "CNPJ identifier", example = "12345678000199")
        String cnpj,
        @Schema(description = "CRECI registration", example = "123456")
        String creci,
        @Schema(description = "Contact phone number", example = "11999999999")
        String phone,
        @Schema(description = "Street address", example = "Rua Augusta, 1500")
        String address,
        @Schema(description = "City name", example = "Sao Paulo")
        String city,
        @Schema(description = "State or province", example = "SP")
        String state,
        @Schema(description = "Country name", example = "Brasil")
        String country,
        @Schema(description = "ZIP or postal code", example = "01305100")
        String zipCode,
        @Schema(description = "Related user identifier", example = "10")
        Long userId,
        @Schema(description = "Related user username", example = "horizonte.agent")
        String username,
        @Schema(description = "Related user e-mail", example = "agent@horizonte.com")
        String email,
        @Schema(description = "Whether the related user is active", example = "true")
        boolean active,
        @Schema(description = "Creation timestamp", example = "2026-04-14T20:00:00")
        LocalDateTime createdAt,
        @Schema(description = "Last update timestamp", example = "2026-04-14T20:15:00")
        LocalDateTime updatedAt
) {
    public RealEstateAgentDTO(RealEstateAgent realEstateAgent) {
        this(
                realEstateAgent.id,
                realEstateAgent.name,
                realEstateAgent.cnpj,
                realEstateAgent.creci,
                realEstateAgent.phone,
                realEstateAgent.address.address,
                realEstateAgent.address.city,
                realEstateAgent.address.state,
                realEstateAgent.address.country,
                realEstateAgent.address.zipCode,
                realEstateAgent.userId,
                realEstateAgent.user.getUsername(),
                realEstateAgent.user.getEmail(),
                realEstateAgent.user.isActive(),
                realEstateAgent.getCreatedAt(),
                realEstateAgent.getUpdatedAt()
        );
    }
}
