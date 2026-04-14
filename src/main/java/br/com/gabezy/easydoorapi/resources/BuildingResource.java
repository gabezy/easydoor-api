package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.building.CreateBuildingRequest;
import br.com.gabezy.easydoorapi.resources.dto.building.FilterBuildingDTO;
import br.com.gabezy.easydoorapi.services.BuildingService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/buildings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BuildingResource {

    private final BuildingService buildingService;

    public BuildingResource(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_BUILDING"})
    public Response createBuilding(@Valid CreateBuildingRequest request) {
        return Response.created(UriBuilder.fromUri("/buildings/{id}").build(buildingService.create(request).id)).build();
    }

    @GET
    @PermitAll
    public Response getAllBuildings(@Valid FilterBuildingDTO filter) {
        return Response.ok(buildingService.findByFilter(filter)).build();
    }

}
