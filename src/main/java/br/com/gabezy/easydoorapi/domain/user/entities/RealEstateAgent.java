package br.com.gabezy.easydoorapi.domain.user.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "real_estate_agents")
public class RealEstateAgent extends BaseUpdatableEntity {

    public String name;
    public String cnpj;
    public String creci;
    public String phone;
    @Embedded
    public Address address;
    @Column(name = "user_id", nullable = false)
    public Long userId;
    @OneToOne
    @NotAudited
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    public User user;

}
