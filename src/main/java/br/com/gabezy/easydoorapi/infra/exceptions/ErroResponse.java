package br.com.gabezy.easydoorapi.infra.exceptions;

public record ErroResponse(
        String error,
        String message,
        int status,
        long timestamp
) {
    public ErroResponse(String error, String message, int status) {
        this(error, message, status, System.currentTimeMillis());
    }
}
