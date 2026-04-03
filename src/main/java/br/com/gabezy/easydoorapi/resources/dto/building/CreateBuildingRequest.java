package br.com.gabezy.easydoorapi.resources.dto.building;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateBuildingRequest(
        @NotEmpty
        String name,
        @NotNull
        BigDecimal longitude,
        @NotNull
        BigDecimal latitude,
        @NotNull
        @Positive
        Double area,
        @NotEmpty
        String description,
        @NotNull
        @Valid
        AddressDTO address
) {}
