package br.com.gabezy.easydoorapi.resources.dto.building;

public record FilterBuildingDTO(
        String name,
        String description,
        Double area,
        Long lockerId
) {}
