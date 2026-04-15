package br.com.gabezy.easydoorapi.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Embeddable
@Schema(name = "GeographicalCoordinates", description = "Geographic coordinates used by persisted entities")
public class GeographicalCoordinates {

    public GeographicalCoordinates() {
    }

    public GeographicalCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Column(nullable = false)
    @Schema(description = "Latitude", example = "-23.55052")
    public BigDecimal latitude;
    @Column(nullable = false)
    @Schema(description = "Longitude", example = "-46.633308")
    public BigDecimal longitude;

}
