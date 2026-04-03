package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CoordinatesDTO(
        @NotNull
        BigDecimal latitude,
        @NotNull
        BigDecimal longitude
) {
    public CoordinatesDTO(double latitude, double longitude) {
        this(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude));
    }
}
