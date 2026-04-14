package br.com.gabezy.easydoorapi.resources.dto.appointment;

import br.com.gabezy.easydoorapi.domain.annotations.DateFormat;
import br.com.gabezy.easydoorapi.domain.annotations.DateTimeFormat;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;

public record FilterAppointmentDTO(
        @QueryParam("buildingId")
        Long buildingId,
        @QueryParam("realEstateAgentId")
        Long realEstateAgentId,
        @QueryParam("clientId")
        Long clientId,
        @QueryParam("dateFrom")
        @DateFormat
        LocalDate dateFrom,
        @QueryParam("dateTo")
        @DateFormat
        LocalDate dateTo,
        @QueryParam("canceled")
        boolean canceled
) {}
