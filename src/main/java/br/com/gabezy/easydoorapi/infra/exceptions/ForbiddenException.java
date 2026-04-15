package br.com.gabezy.easydoorapi.infra.exceptions;

public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(message, 403, "Forbidden");
    }
}
