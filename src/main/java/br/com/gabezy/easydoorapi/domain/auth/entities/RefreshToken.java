package br.com.gabezy.easydoorapi.domain.auth.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends PanacheEntity {

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    public String token;

    @Column(nullable = false)
    public Long userId;

    @Column(nullable = false)
    public LocalDateTime expiresAt;

    @Column
    public LocalDateTime revokedAt;

    @Column(nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public RefreshToken() {
    }

    public RefreshToken(String token, Long userId, LocalDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public boolean isValid() {
        return this.revokedAt == null && LocalDateTime.now().isBefore(this.expiresAt);
    }

    public void revoke() {
        this.revokedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "userId=" + userId +
                ", expiresAt=" + expiresAt +
                ", revokedAt=" + revokedAt +
                '}';
    }
}

