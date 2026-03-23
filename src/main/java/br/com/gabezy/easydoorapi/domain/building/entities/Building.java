package br.com.gabezy.easydoorapi.domain.building.entities;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Locker;
import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.shared.entities.GeographicalCoordinates;
import jakarta.persistence.*;

@Entity
@Table(name = "buildings")
public class Building extends BaseUpdatableEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "locker_id")
    private Long lockerId;

    @OneToOne
    @JoinColumn(name = "locker_id", updatable = false, insertable = false)
    private Locker locker;

    @Embedded
    private GeographicalCoordinates coordinates;

    @Column(nullable = false)
    private Double area;

    @Column(nullable = false)
    private String description;

    @Embedded
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
