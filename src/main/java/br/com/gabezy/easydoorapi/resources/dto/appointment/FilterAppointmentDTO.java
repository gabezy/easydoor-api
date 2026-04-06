package br.com.gabezy.easydoorapi.resources.dto.appointment;

import java.time.LocalDate;

public record FilterAppointmentDTO(
        Long buildingId,
        Long realEstateAgentId,
        Long clientId,
        LocalDate dateFrom,
        LocalDate dateTo,
        boolean canceled
) {}
