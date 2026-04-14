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

    public String name;

    public String cpf;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @OneToOne
    @NotAudited
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)
    public User user;

    public Client(String name, String cpf, Long userId) {
        this.name = name;
        this.cpf = new Cpf(cpf).value();
        this.userId = userId;
    }

    public Client() {}

}
