package br.com.gabezy.easydoorapi.domain.shared;

import java.util.Objects;

/**
 * Value Object representing a JWT Token
 */
public record Token (String value) {

    public Token {
        if (Objects.isNull(value)|| value.trim().isEmpty()) {
            throw new IllegalArgumentException("Token must not be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Token{***}";
    }
}

