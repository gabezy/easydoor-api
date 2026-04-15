package br.com.gabezy.easydoorapi.resources.dto.realestateagent;

import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;

import java.time.LocalDateTime;

public record RealEstateAgentDTO(
        Long id,
        String name,
        String cnpj,
        String creci,
        String phone,
        String address,
        String city,
        String state,
        String country,
        String zipCode,
        Long userId,
        String username,
        String email,
        boolean active,
        LocalDateTime createdAt,
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
