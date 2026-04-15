package br.com.gabezy.easydoorapi.resources.dto.building;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Schema(name = "CreateBuildingRequest", description = "Payload used to create a building")
public record CreateBuildingRequest(
        @Schema(description = "Building name", example = "Edificio Central")
        @NotBlank
        String name,
        @Schema(description = "Building geographic coordinates")
        @NotNull
        @Valid
        CoordinatesDTO coordinates,
        @Schema(description = "Total building area in square meters", example = "500.0")
        @NotNull
        @Positive
        Double area,
        @Schema(description = "Building description", example = "Edificio residencial com portaria 24h")
        @NotBlank
        @Size(max = 255)
        String description,
        @Schema(description = "Building address")
        @NotNull
        @Valid
        AddressDTO address,
        @Schema(description = "Locker identifier associated with the building", example = "1")
        @NotNull
        Long lockerId
) {}
