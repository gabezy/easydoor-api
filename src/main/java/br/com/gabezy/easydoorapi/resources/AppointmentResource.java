package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.domain.appointment.entities.Appontiment;
import br.com.gabezy.easydoorapi.infra.exceptions.ErroResponse;
import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.appointment.AppointmentApprovalRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import br.com.gabezy.easydoorapi.services.AppointmentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Appointments", description = "Appointment management endpoints")
@SecurityRequirement(name = "JWT")
public class AppointmentResource {

    @Inject
    private JsonWebToken jwt;

    private final AppointmentService service;

    public AppointmentResource(AppointmentService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_APPOINTMENT"})
    @Operation(summary = "Create an appointment", description = "Creates a new appointment. Access is restricted to ADMIN or CREATE_APPOINTMENT.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Appointment created successfully",
                    headers = @Header(name = "Location", description = "URL of the created appointment resource",
                            schema = @Schema(type = SchemaType.STRING, example = "/appointments/1"))),
            @APIResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to create appointments")
    })
    public Response createAppointment(@Valid CreateAppointmentRequest request) {
        var appointment = service.createAppointment(request);
        return Response.created(UriBuilder.fromUri("/appointments/{id}").build(appointment.id))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_APPOINTMENTS"})
    @Operation(summary = "List appointments", description = "Returns appointments filtered by the provided query parameters. Access is restricted to ADMIN or VIEW_APPOINTMENTS.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Appointments returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = Appontiment.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view appointments")
    })
    public Response getAllAppointments(@Valid FilterAppointmentDTO filter) {
        JsonNumber userId = jwt.getClaim("user_id");
        return Response.ok(service.findByFilter(filter, userId.longValueExact())).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "VIEW_APPOINTMENT"})
    @Operation(summary = "Get appointment by id", description = "Returns a single appointment by identifier. Access is restricted to ADMIN or VIEW_APPOINTMENT.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Appointment returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Appontiment.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view the appointment"),
            @APIResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErroResponse.class)))
    })
    public Response getAppointmentById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @PATCH
    @Path("/{id}/approval")
    @RolesAllowed({"ADMIN", "APPROVE_APPOINTMENTS"})
    @Operation(
            summary = "Approve or reject an appointment",
            description = "Approves or rejects an appointment. Access is restricted to ADMIN or APPROVE_APPOINTMENTS. The informed approvedUserId must belong to an ADMIN user or to the real estate agent associated with the appointment."
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Appointment decision registered successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Appontiment.class))),
            @APIResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to approve this appointment",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErroResponse.class))),
            @APIResponse(responseCode = "404", description = "Appointment or decision user not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErroResponse.class)))
    })
    public Response reviewAppointment(@PathParam("id") Long id, @Valid AppointmentApprovalRequest request) {
        return Response.ok(service.reviewAppointment(id, request)).build();
    }

    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to Long");
    }

}
