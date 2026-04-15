package br.com.gabezy.easydoorapi.resources.dto.building;

import br.com.gabezy.easydoorapi.resources.dto.CoordinatesDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CreateLockerRequest", description = "Payload used to create a locker")
public record CreateLockerRequest (
        @Schema(description = "Locker display name", example = "Locker Paulista")
        @NotBlank
        String name,
        @Schema(description = "Unique serial number", example = "LOCKER-001")
        @NotBlank
        String serialNumber,
        @Schema(description = "Locker geographic coordinates")
        @NotNull
        @Valid
        CoordinatesDTO coordinates
) {}
