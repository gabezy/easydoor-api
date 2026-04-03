package br.com.gabezy.easydoorapi.domain.building.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.BaseEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table( name = "lockers")
public class Locker extends BaseEntity {

    @Column(unique = true, nullable = false)
    public String serialNumber;

    public String name;

    @Embedded
    public GeographicalCoordinates coordinates;

}
