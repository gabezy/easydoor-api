package br.com.gabezy.easydoorapi.domain.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public abstract class BaseUpdatableEntity extends BaseEntity {

    @Column
    protected LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
}
