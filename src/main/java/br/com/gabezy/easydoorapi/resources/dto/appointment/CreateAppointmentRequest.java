package br.com.gabezy.easydoorapi.resources.dto.appointment;

import br.com.gabezy.easydoorapi.domain.annotations.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "CreateAppointmentRequest", description = "Payload used to create an appointment")
public record CreateAppointmentRequest(
        @Schema(description = "Appointment date and time", example = "2026-04-20 14:30:00")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime time,
        @Schema(description = "Client identifier", example = "1")
        @NotNull
        Long userId,
        @Schema(description = "Real estate agent identifier", example = "1")
        @NotNull
        Long realEstateAgentId,
        @Schema(description = "Building identifier", example = "1")
        @NotNull
        Long buildingId
) {}
