package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.resources.dto.appointment.FilterAppointmentDTO;
import br.com.gabezy.easydoorapi.services.AppointmentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentResource {

    private final AppointmentService service;

    public AppointmentResource(AppointmentService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_APPOINTMENT"})
    public Response createAppointment(@Valid CreateAppointmentRequest request) {
        var appointment = service.createAppointment(request);
        return Response.created(UriBuilder.fromUri("/appointments/{id}").build(appointment.id))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_APPOINTMENT"})
    public Response getAllAppointments(@Valid FilterAppointmentDTO filter) {
        return Response.ok(service.findByFilter(filter)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "VIEW_APPOINTMENT"})
    public Response getAppointmentById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

}
