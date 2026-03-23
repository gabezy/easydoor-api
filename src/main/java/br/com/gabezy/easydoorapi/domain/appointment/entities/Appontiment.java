package br.com.gabezy.easydoorapi.domain.appointment.entities;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.user.entities.Client;
import br.com.gabezy.easydoorapi.domain.user.entities.RealEstateAgent;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;

@Entity
@Audited
@Table(name = "appointments")
public class Appontiment extends BaseUpdatableEntity {

    private LocalDateTime time;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    @NotAudited
    private Client client;

    @Column(name = "real_estate_agent_id", nullable = false)
    private Long realEstateAgentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_agent_id", updatable = false, insertable = false)
    @NotAudited
    private RealEstateAgent realEstateAgent;

    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", updatable = false, insertable = false)
    @NotAudited
    private Building building;

    private LocalDateTime canceledAt;

    private LocalDateTime approvedAt;

    private LocalDateTime finishedAt;

    private int rating;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Client getClient() {
        return client;
    }

    public Long getRealEstateAgentId() {
        return realEstateAgentId;
    }

    public void setRealEstateAgentId(Long realEstateAgentId) {
        this.realEstateAgentId = realEstateAgentId;
    }

    public RealEstateAgent getRealEstateAgent() {
        return realEstateAgent;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Building getBuilding() {
        return building;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
