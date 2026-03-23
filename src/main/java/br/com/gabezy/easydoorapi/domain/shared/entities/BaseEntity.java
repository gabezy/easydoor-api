package br.com.gabezy.easydoorapi.domain.shared.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    protected LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
