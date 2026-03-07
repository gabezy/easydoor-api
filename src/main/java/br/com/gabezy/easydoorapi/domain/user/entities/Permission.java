package br.com.gabezy.easydoorapi.domain.user.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Permission Entity - represents a system permission with a unique String code
 */
@Entity
@Table(name = "permissions")
public class Permission extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 100)
    public String code;

    @Column(nullable = false, length = 255)
    public String description;

    @Column(nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(nullable = false)
    public LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Permission() {
    }

    public Permission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

