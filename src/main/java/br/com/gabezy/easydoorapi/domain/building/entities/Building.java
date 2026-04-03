package br.com.gabezy.easydoorapi.domain.building.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.*;

@Entity
@Table(name = "buildings")
public class Building extends BaseUpdatableEntity {

    public Building() {}

    public Building(String name, Locker locker, Double area, String description, Address address) {
        this.name = name;
        this.locker = locker;
        this.area = area;
        this.description = description;
        this.address = address;
    }

    @Column(nullable = false)
    public String name;

    @Column(name = "locker_id", nullable = false)
    public Long lockerId;

    @OneToOne
    @JoinColumn(name = "locker_id", updatable = false, insertable = false)
    public Locker locker;

    @Embedded
    public GeographicalCoordinates coordinates;

    @Column(nullable = false)
    public Double area;

    @Column(nullable = false)
    public String description;

    @Embedded
    public Address address;

}
