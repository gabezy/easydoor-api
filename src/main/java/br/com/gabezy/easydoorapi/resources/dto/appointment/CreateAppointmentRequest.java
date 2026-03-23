package br.com.gabezy.easydoorapi.resources.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime time,
        @NotNull
        Long clientId,
        @NotNull
        Long realEstateAgentId,
        @NotNull
        Long buildingId
) {}
