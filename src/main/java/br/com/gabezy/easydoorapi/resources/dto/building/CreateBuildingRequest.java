package br.com.gabezy.easydoorapi.resources.dto.building;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


public record CreateBuildingRequest(
        @NotBlank
        String name,
        @NotNull
        @Valid
        CoordinatesDTO coordinates,
        @NotNull
        @Positive
        Double area,
        @NotBlank
        @Size(max = 255)
        String description,
        @NotNull
        @Valid
        AddressDTO address,
        @NotNull
        Long lockerId
) {}
