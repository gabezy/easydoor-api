package br.com.gabezy.easydoorapi.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {

    @Column
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

}
