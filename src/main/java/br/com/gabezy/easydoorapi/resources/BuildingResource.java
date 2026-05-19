package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.building.entities.Building;
import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/buildings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Buildings", description = "Building management endpoints")
public class BuildingResource {

    private final BuildingService buildingService;

    public BuildingResource(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_BUILDING"})
    @Operation(summary = "Create a building", description = "Creates a new building. Access is restricted to ADMIN or CREATE_BUILDING.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Building created successfully",
                    headers = @Header(name = "Location", description = "URL of the created building resource",
                            schema = @Schema(type = SchemaType.STRING, example = "/buildings/1"))),
            @APIResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to create buildings")
    })
    public Response createBuilding(@Valid CreateBuildingRequest request) {
        return Response.created(UriBuilder.fromUri("/buildings/{id}").build(buildingService.create(request).id)).build();
    }

    @GET
    @PermitAll
    @Operation(summary = "List buildings", description = "Returns buildings filtered by the provided query parameters. Public endpoint.")
    @APIResponse(responseCode = "200", description = "Buildings returned successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Building.class)))
    public Response getAllBuildings(@Valid FilterBuildingDTO filter) {
        System.out.println("Received filter: " + filter);
        return Response.ok(buildingService.findByFilter(filter)).build();
    }

}
