package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "Coordinates", description = "Geographic coordinates")
public record CoordinatesDTO(
        @Schema(description = "Latitude", example = "-23.55052")
        @NotNull
        BigDecimal latitude,
        @Schema(description = "Longitude", example = "-46.633308")
        @NotNull
        BigDecimal longitude
) {
    public CoordinatesDTO(double latitude, double longitude) {
        this(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude));
    }
}
