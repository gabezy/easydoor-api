package br.com.gabezy.easydoorapi.infra.exceptions;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message, 404, "Not Found");
    }

}
