package br.com.gabezy.easydoorapi.infra.exceptions;

public abstract class ApiException extends RuntimeException {

    protected final int statusCode;
    protected final String error;

    public ApiException(String message, int statusCode, String error) {
        super(message);
        this.statusCode = statusCode;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }
}
