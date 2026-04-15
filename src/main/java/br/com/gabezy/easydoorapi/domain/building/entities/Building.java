package br.com.gabezy.easydoorapi.domain.building.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table(name = "buildings")
@Schema(name = "Building", description = "Building registered in the system")
public class Building extends BaseUpdatableEntity {

    public Building() {}

    public Building(String name, Long lockerId, GeographicalCoordinates coordinates, Double area, String description, Address address) {
        this.name = name;
        this.lockerId = lockerId;
        this.coordinates = coordinates;
        this.area = area;
        this.description = description;
        this.address = address;
    }

    @Column(nullable = false)
    @Schema(description = "Building name", example = "Edificio Central")
    public String name;

    @Column(name = "locker_id", nullable = false)
    @Schema(description = "Locker identifier associated with the building", example = "1")
    public Long lockerId;

    @OneToOne
    @JoinColumn(name = "locker_id", updatable = false, insertable = false)
    public Locker locker;

    @Embedded
    @Schema(description = "Geographic coordinates")
    public GeographicalCoordinates coordinates;

    @Column(nullable = false)
    @Schema(description = "Building area", example = "500.0")
    public Double area;

    @Column(nullable = false)
    @Schema(description = "Building description", example = "Edificio residencial com portaria 24h")
    public String description;

    @Embedded
    @Schema(description = "Address")
    public Address address;

}
