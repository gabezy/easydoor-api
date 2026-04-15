package br.com.gabezy.easydoorapi.domain.appointment.entities;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import br.com.gabezy.easydoorapi.domain.user.entities.User;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;

@Entity
@Audited
@Table(name = "appointments")
@Schema(name = "Appointment", description = "Appointment scheduled between a client, a real estate agent, and a building")
public class Appontiment extends BaseUpdatableEntity {

    public Appontiment() {}

    public Appontiment(LocalDateTime time, Long clientId, Long realEstateAgentId, Long buildingId,
                       LocalDateTime canceledAt, LocalDateTime approvedAt, LocalDateTime rejectedAt, LocalDateTime finishedAt,
                       Integer rating) {
        this.time = time;
        this.clientId = clientId;
        this.realEstateAgentId = realEstateAgentId;
        this.buildingId = buildingId;
        this.canceledAt = canceledAt;
        this.approvedAt = approvedAt;
        this.rejectedAt = rejectedAt;
        this.finishedAt = finishedAt;
        this.rating = rating;
    }

    @Column(nullable = false)
    @Schema(description = "Appointment date and time", example = "2026-04-20T14:30:00")
    public LocalDateTime time;

    @Column(name = "client_id", nullable = false)
    @Schema(description = "Client identifier", example = "1")
    public Long clientId;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    public Client client;

    @Column(name = "real_estate_agent_id", nullable = false)
    @Schema(description = "Real estate agent identifier", example = "1")
    public Long realEstateAgentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "real_estate_agent_id", updatable = false, insertable = false)
    @NotAudited
    public RealEstateAgent realEstateAgent;

    @Column(name = "building_id", nullable = false)
    @Schema(description = "Building identifier", example = "1")
    public Long buildingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_id", updatable = false, insertable = false)
    @NotAudited
    public Building building;

    @Column(name = "approved_user_id")
    @Schema(description = "User identifier that approved or rejected the appointment", example = "1")
    public Long approvedUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_user_id", updatable = false, insertable = false)
    @NotAudited
    public User approvedUser;

    @Schema(description = "Cancellation timestamp if the appointment was canceled", example = "2026-04-18T10:00:00")
    public LocalDateTime canceledAt;

    @Schema(description = "Approval timestamp if the appointment was approved", example = "2026-04-18T11:00:00")
    public LocalDateTime approvedAt;

    @Schema(description = "Rejection timestamp if the appointment was rejected", example = "2026-04-18T11:00:00")
    public LocalDateTime rejectedAt;

    @Schema(description = "Completion timestamp if the appointment was completed", example = "2026-04-20T15:30:00")
    public LocalDateTime finishedAt;

    @Schema(description = "Rating given to the appointment", example = "5")
    public Integer rating;

}
