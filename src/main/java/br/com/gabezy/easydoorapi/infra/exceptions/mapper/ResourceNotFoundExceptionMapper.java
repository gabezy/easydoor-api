package br.com.gabezy.easydoorapi.infra.exceptions.mapper;

import br.com.gabezy.easydoorapi.infra.exceptions.ErroResponse;
import br.com.gabezy.easydoorapi.infra.exceptions.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        return Response
                .status(exception.getStatusCode())
                .entity(new ErroResponse("NOT_FOUND", exception.getMessage(), exception.getStatusCode()))
                .build();
    }

}
