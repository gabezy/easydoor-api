package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.realestateagent.CreateRealEstateAgentRequest;
import br.com.gabezy.easydoorapi.services.RealEstateAgentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/real-estate-agents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RealEstateAgentResource {

    private final RealEstateAgentService service;

    public RealEstateAgentResource(RealEstateAgentService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_REAL_ESTATE_AGENT"})
    public Response create(@Valid CreateRealEstateAgentRequest request) {
        var realEstateAgent = service.create(request);
        return Response.created(
                UriBuilder.fromUri("/real-estate-agents/{id}").build(realEstateAgent.id)
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_REAL_ESTATE_AGENTS"})
    public Response getAll() {
        return Response.ok(service.findAll()).build();
    }
}
