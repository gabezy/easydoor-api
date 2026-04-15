package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.building.entities.Locker;
import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import br.com.gabezy.easydoorapi.services.LockerService;
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
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/lockers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Lockers", description = "Locker management endpoints")
@SecurityRequirement(name = "JWT")
public class LockerResource {

    private final LockerService lockerService;

    public LockerResource(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_LOCKER"})
    @Operation(summary = "Create a locker", description = "Creates a new locker. Access is restricted to ADMIN or CREATE_LOCKER.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Locker created successfully",
                    headers = @Header(name = "Location", description = "URL of the created locker resource",
                            schema = @Schema(type = SchemaType.STRING, example = "/lockers/1"))),
            @APIResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to create lockers")
    })
    public Response createLocker(@Valid CreateLockerRequest request) {
        var locker = lockerService.create(request);
        return Response.created(UriBuilder.fromUri("/lockers/{id}").build(locker.id)).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_LOCKERS"})
    @Operation(summary = "List lockers", description = "Returns all lockers. Access is restricted to ADMIN or VIEW_LOCKERS.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lockers returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Locker.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view lockers")
    })
    public Response getAllLockers() {
        return Response.ok(lockerService.findAll()).build();
    }

}
