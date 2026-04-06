package br.com.gabezy.easydoorapi.infra.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private int statusCode = 404;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
