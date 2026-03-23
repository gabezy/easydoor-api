package br.com.gabezy.easydoorapi.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class GeographicalCoordinates {

    @Column(nullable = false)
    private BigDecimal latitude;
    @Column(nullable = false)
    private BigDecimal longitude;

}
