package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.infra.exceptions.ErroResponse;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.CreateRealEstateAgentRequest;
import br.com.gabezy.easydoorapi.resources.dto.realestateagent.RealEstateAgentDTO;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/real-estate-agents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Real Estate Agents", description = "Operations for creating and listing real estate agents")
@SecurityRequirement(name = "JWT")
public class RealEstateAgentResource {

    private final RealEstateAgentService service;

    public RealEstateAgentResource(RealEstateAgentService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_REAL_ESTATE_AGENT"})
    @Operation(
            summary = "Create a real estate agent",
            description = "Creates a new real estate agent and its related user in the same transaction. Access is restricted to ADMIN or CREATE_REAL_ESTATE_AGENT."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Real estate agent created successfully",
                    headers = @Header(
                            name = "Location",
                            description = "URL of the created real estate agent resource",
                            schema = @Schema(type = SchemaType.STRING, example = "/real-estate-agents/20")
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid request or duplicated e-mail/username",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErroResponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "User does not have permission to create real estate agents"
            )
    })
    public Response create(@Valid CreateRealEstateAgentRequest request) {
        var realEstateAgent = service.create(request);
        return Response.created(
                UriBuilder.fromUri("/real-estate-agents/{id}").build(realEstateAgent.id)
        ).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_REAL_ESTATE_AGENTS"})
    @Operation(
            summary = "List real estate agents",
            description = "Returns all registered real estate agents with their related user information. Access is restricted to ADMIN or VIEW_REAL_ESTATE_AGENTS."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of real estate agents returned successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    type = SchemaType.ARRAY,
                                    implementation = RealEstateAgentDTO.class
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "User does not have permission to view real estate agents"
            )
    })
    public Response getAll() {
        return Response.ok(service.findAll()).build();
    }
}
