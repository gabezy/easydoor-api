package br.com.gabezy.easydoorapi.infra.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        var entity = new ErroResponse(exception.getError(), exception.getMessage(), exception.getStatusCode());

        return Response
                .status(exception.getStatusCode())
                .entity(entity)
                .build();
    }

}
