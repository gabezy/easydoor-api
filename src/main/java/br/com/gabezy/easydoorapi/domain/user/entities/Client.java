package br.com.gabezy.easydoorapi.domain.user.entities;

import br.com.gabezy.easydoorapi.domain.shared.entities.BaseUpdatableEntity;
import br.com.gabezy.easydoorapi.domain.shared.vo.Cpf;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "clients")
public class Client extends BaseUpdatableEntity {

    private String name;

    private String cpf;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne
    @NotAudited
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)
    private User user;

    public Client(String name, String cpf, Long userId) {
        this.name = name;
        this.cpf = new Cpf(cpf).value();
        this.userId = userId;
    }

    public Client() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
