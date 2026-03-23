package br.com.gabezy.easydoorapi.domain.appointment.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.BaseEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table( name = "lockers")
public class Locker extends BaseEntity {

    @Column(unique = true)
    private String serialNumber;

    private String name;

    @Embedded
    private GeographicalCoordinates coordinates;



}
