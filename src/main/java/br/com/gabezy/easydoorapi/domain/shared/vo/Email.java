package br.com.gabezy.easydoorapi.domain.shared.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing an Email address
 */
public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    public Email {
        if (Objects.isNull(value) || value.isBlank()) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("Email must not be empty");
            }
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Email format is invalid");
        }

        value = value.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email(String value1))) {
            return false;
        }
        return Objects.equals(value, value1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

}

