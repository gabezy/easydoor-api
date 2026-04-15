package br.com.gabezy.easydoorapi.resources.dto.building;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "FilterBuilding", description = "Query parameters used to filter buildings")
public record FilterBuildingDTO(
        @Schema(description = "Filter by building name", example = "Central")
        String name,
        @Schema(description = "Filter by description", example = "residencial")
        String description,
        @Schema(description = "Filter by area", example = "500.0")
        Double area,
        @Schema(description = "Filter by locker identifier", example = "1")
        Long lockerId
) {}
