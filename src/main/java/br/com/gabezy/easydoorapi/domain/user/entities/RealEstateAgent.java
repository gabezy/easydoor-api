package br.com.gabezy.easydoorapi.domain.user.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.Address;
import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "real_estate_agents")
public class RealEstateAgent extends BaseUpdatableEntity {

    private String name;
    private String cnpj;
    private String creci;
    private String phone;
    @Embedded
    private Address address;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)
    @NotAudited
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCreci() {
        return creci;
    }

    public void setCreci(String creci) {
        this.creci = creci;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

}
