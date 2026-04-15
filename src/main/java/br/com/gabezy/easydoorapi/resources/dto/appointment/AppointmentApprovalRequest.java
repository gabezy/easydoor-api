package br.com.gabezy.easydoorapi.resources.dto.appointment;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "AppointmentApprovalRequest", description = "Payload used to approve or reject an appointment")
public record AppointmentApprovalRequest(
        @Schema(description = "Identifier of the user performing the decision", example = "1")
        @NotNull
        Long approvedUserId,
        @Schema(description = "Decision flag. True approves the appointment, false rejects it", example = "true")
        @NotNull
        Boolean approved
) {
}
