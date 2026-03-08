package br.com.gabezy.easydoorapi.domain.shared;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public abstract class BaseEntity extends PanacheEntity {

    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return super.id;
    }
}
