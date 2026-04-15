package br.com.gabezy.easydoorapi.resources.dto;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Address", description = "Address information used in resource creation and listing responses")
public record AddressDTO(
   @Schema(description = "Street address", example = "Rua Augusta, 1500")
   @NotBlank
   String address,
   @Schema(description = "City name", example = "Sao Paulo")
   @NotBlank
   String city,
   @Schema(description = "State or province", example = "SP")
   @NotBlank
   String state,
   @Schema(description = "Country name", example = "Brasil")
   @NotBlank
   String country,
   @Schema(description = "ZIP or postal code", example = "01305-100")
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
