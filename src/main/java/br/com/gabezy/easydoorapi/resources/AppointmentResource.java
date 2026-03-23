package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.appointment.CreateAppointmentRequest;
import br.com.gabezy.easydoorapi.services.AppointmentService;
import br.com.gabezy.easydoorapi.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    public Response createAppointment(@Valid CreateAppointmentRequest request) {
        var appointment = service.createAppointment(request);
        return Response.created(UriBuilder.fromUri("/appointments/{id}").build(appointment.id))
                .build();
    }

}
