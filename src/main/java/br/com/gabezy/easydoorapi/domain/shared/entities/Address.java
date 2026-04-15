package br.com.gabezy.easydoorapi.domain.shared.entities;

import br.com.gabezy.easydoorapi.resources.dto.AddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Embeddable
@Schema(name = "PersistedAddress", description = "Address representation stored in persisted entities")
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
    @Schema(description = "Street address", example = "Rua Augusta, 1500")
    public String address;

    @Column(nullable = false)
    @Schema(description = "City name", example = "Sao Paulo")
    public String city;

    @Column(nullable = false)
    @Schema(description = "State or province", example = "SP")
    public String state;

    @Column(nullable = false)
    @Schema(description = "Country name", example = "Brasil")
    public String country;

    @Column(nullable = false)
    @Schema(description = "ZIP or postal code", example = "01305100")
    public String zipCode;

}
