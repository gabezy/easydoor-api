package br.com.gabezy.easydoorapi.resources.dto;

import jakarta.validation.constraints.NotEmpty;

public record AddressDTO(
   @NotEmpty
   String address,
   @NotEmpty
   String city,
   @NotEmpty
   String state,
   @NotEmpty
   String country,
   @NotEmpty
   String zipCode
) {}
