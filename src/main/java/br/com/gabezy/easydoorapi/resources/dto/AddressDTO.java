package br.com.gabezy.easydoorapi.resources.dto;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
   @NotBlank
   String address,
   @NotBlank
   String city,
   @NotBlank
   String state,
   @NotBlank
   String country,
   @NotBlank
   String zipCode
) {
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj == null) {
         return false;
      }

      if (obj instanceof Address address) {
         return this.address.equals(address.address) && this.city.equals(address.city)
                 && this.state.equals(address.state) && this.country.equals(address.country)
                 && this.zipCode.equals(address.zipCode);
      }

      if (getClass() != obj.getClass()) {
         return false;
      }

      AddressDTO other = (AddressDTO) obj;
      return address.equals(other.address) && city.equals(other.city)
              && state.equals(other.state) && country.equals(other.country)
              && zipCode.equals(other.zipCode);
   }
}
