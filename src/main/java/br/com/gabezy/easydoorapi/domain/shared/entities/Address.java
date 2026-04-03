package br.com.gabezy.easydoorapi.domain.shared.entities;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    public Address() {
    }

    public Address(String address, String city, String state, String country, String zipCode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode.replace("-", "");
    }

    @Column(nullable = false)
    public String address;

    @Column(nullable = false)
    public String city;

    @Column(nullable = false)
    public String state;

    @Column(nullable = false)
    public String country;

    @Column(nullable = false)
    public String zipCode;

}
