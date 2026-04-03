package br.com.gabezy.easydoorapi.resources.dto.building;

import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLockerRequest (
        @NotBlank
        String name,
        @NotBlank
        String serialNumber,
        @NotNull
        @Valid
        CoordinatesDTO coordinates
) {}
