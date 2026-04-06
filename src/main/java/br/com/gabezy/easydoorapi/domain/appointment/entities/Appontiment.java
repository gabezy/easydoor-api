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

    public Appontiment() {}

    public Appontiment(LocalDateTime time, Long clientId, Long realEstateAgentId, Long buildingId,
                       LocalDateTime canceledAt, LocalDateTime approvedAt, LocalDateTime finishedAt,
                       Integer rating) {
        this.time = time;
        this.clientId = clientId;
        this.realEstateAgentId = realEstateAgentId;
        this.buildingId = buildingId;
        this.canceledAt = canceledAt;
        this.approvedAt = approvedAt;
        this.finishedAt = finishedAt;
        this.rating = rating;
    }

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(name = "client_id", nullable = false)
    public Long clientId;

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    public Client client;

    @Column(name = "real_estate_agent_id", nullable = false)
    public Long realEstateAgentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "real_estate_agent_id", updatable = false, insertable = false)
    @NotAudited
    public RealEstateAgent realEstateAgent;

    @Column(name = "building_id", nullable = false)
    public Long buildingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_id", updatable = false, insertable = false)
    @NotAudited
    public Building building;

    public LocalDateTime canceledAt;

    public LocalDateTime approvedAt;

    public LocalDateTime finishedAt;

    public Integer rating;

}
