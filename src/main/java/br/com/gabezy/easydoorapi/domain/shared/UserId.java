package br.com.gabezy.easydoorapi.domain.shared;

import java.util.Objects;

/**
 * Value Object representing a User ID
 */
public class UserId {
    private final Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("UserId must be a positive number");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserId{" + value + '}';
    }
}

