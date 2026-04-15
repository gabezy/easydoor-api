package br.com.gabezy.easydoorapi.domain.building.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.BaseEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Table( name = "lockers")
@Schema(name = "Locker", description = "Locker available to be associated with buildings")
public class Locker extends BaseEntity {

    public Locker() {};

    public Locker(String serialNumber, String name, GeographicalCoordinates coordinates) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.coordinates = coordinates;
    }

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique serial number", example = "LOCKER-001")
    public String serialNumber;

    @Schema(description = "Locker display name", example = "Locker Paulista")
    public String name;

    @Embedded
    @Schema(description = "Geographic coordinates")
    public GeographicalCoordinates coordinates;

}
