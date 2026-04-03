package br.com.gabezy.easydoorapi.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class GeographicalCoordinates {

    public GeographicalCoordinates() {
    }

    public GeographicalCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Column(nullable = false)
    public BigDecimal latitude;
    @Column(nullable = false)
    public BigDecimal longitude;

}
