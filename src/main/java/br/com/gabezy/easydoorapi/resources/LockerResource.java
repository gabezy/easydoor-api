package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.building.CreateLockerRequest;
import br.com.gabezy.easydoorapi.services.LockerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/lockers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LockerResource {

    private final LockerService lockerService;

    public LockerResource(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    @POST
    @RolesAllowed({"ADMIN", "CREATE_LOCKER"})
    public Response createLocker(@Valid CreateLockerRequest request) {
        var locker = lockerService.create(request);
        return Response.created(UriBuilder.fromUri("/lockers/{id}").build(locker.id)).build();
    }

}
