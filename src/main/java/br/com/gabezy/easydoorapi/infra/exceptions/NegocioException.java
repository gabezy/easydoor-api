package br.com.gabezy.easydoorapi.infra.exceptions;

public class NegocioException extends ApiException {

    public NegocioException(String message) {
        super(message, 400, "Bad Request");
    }

    public NegocioException(String message, int statusCode, String error) {
        super(message, statusCode, error);
    }

}
