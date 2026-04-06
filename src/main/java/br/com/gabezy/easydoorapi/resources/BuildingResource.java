package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/buildings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BuildingResource {

    @POST
    @RolesAllowed({"ADMIN", "CREATE_BUILDING"})
    public Response createBuilding(@Valid CreateBuildingRequest request) {
        return Response.ok().build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_BUILDINGS"})
    public Response getAllBuildings() {
        return Response.ok().build();
    }

}
