package br.com.gabezy.easydoorapi.resources.dto.appointment;

import br.com.gabezy.easydoorapi.domain.annotations.DateFormat;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "FilterAppointment", description = "Query parameters used to filter appointments")
public record FilterAppointmentDTO(
        @Schema(description = "Filter by building identifier", example = "1")
        @QueryParam("buildingId")
        Long buildingId,
        @Schema(description = "Filter by real estate agent identifier", example = "1")
        @QueryParam("realEstateAgentId")
        Long realEstateAgentId,
        @Schema(description = "Filter by client identifier", example = "1")
        @QueryParam("clientId")
        Long clientId,
        @Schema(description = "Filter appointments from this date", example = "2026-04-20")
        @QueryParam("dateFrom")
        @DateFormat
        LocalDate dateFrom,
        @Schema(description = "Filter appointments until this date", example = "2026-04-30")
        @QueryParam("dateTo")
        @DateFormat
        LocalDate dateTo,
        @Schema(description = "Whether to return canceled appointments", example = "false")
        @QueryParam("canceled")
        boolean canceled
) {}
