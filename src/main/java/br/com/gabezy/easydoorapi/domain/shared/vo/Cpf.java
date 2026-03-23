package br.com.gabezy.easydoorapi.domain.shared.vo;

public record Cpf(String value) {
    public Cpf {
        if (!value.matches("[0-9]{11}")) {
            throw new IllegalArgumentException("CPF Invalid. CPF must be 11 digits");
        }
    }
}
