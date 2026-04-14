package br.com.gabezy.easydoorapi.resources.dto.appointment;

import br.com.gabezy.easydoorapi.domain.annotations.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull
        @DateTimeFormat
        LocalDateTime time,
        @NotNull
        Long clientId,
        @NotNull
        Long realEstateAgentId,
        @NotNull
        Long buildingId
) {}
