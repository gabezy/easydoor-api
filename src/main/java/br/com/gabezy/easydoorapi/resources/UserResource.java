package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.UserDTO;
import br.com.gabezy.easydoorapi.services.UserApplicationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserApplicationService userApplicationService;

    public UserResource(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response getAllUsers() {
        try {
            List<UserDTO> users = userApplicationService.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch users\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response getUserById(@PathParam("id") Long userId) {
        try {
            UserDTO user = userApplicationService.getUserById(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"User not found\"}")
                        .build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch user\"}")
                    .build();
        }
    }

    @GET
    @Path("/username/{username}")
    @RolesAllowed("ADMIN")
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            UserDTO user = userApplicationService.getUserByUsername(username);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"User not found\"}")
                        .build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch user\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateUser(@PathParam("id") Long userId,
                               @QueryParam("email") String email,
                               @QueryParam("active") Boolean active) {
        try {
            boolean activeStatus = active != null ? active : true;
            UserDTO user = userApplicationService.updateUser(userId, email, activeStatus);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"User not found\"}")
                        .build();
            }
            return Response.ok(user).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to update user\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteUser(@PathParam("id") Long userId) {
        try {
            userApplicationService.deleteUser(userId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to delete user\"}")
                    .build();
        }
    }

    @POST
    @Path("/{id}/roles/{roleName}")
    @RolesAllowed("ADMIN")
    public Response assignRoleToUser(@PathParam("id") Long userId, @PathParam("roleName") String roleName) {
        try {
            userApplicationService.assignRoleToUser(userId, roleName);
            return Response.ok("{\"message\":\"Role assigned successfully\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to assign role\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}/roles/{roleName}")
    @RolesAllowed("ADMIN")
    public Response removeRoleFromUser(@PathParam("id") Long userId, @PathParam("roleName") String roleName) {
        try {
            userApplicationService.removeRoleFromUser(userId, roleName);
            return Response.ok("{\"message\":\"Role removed successfully\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to remove role\"}")
                    .build();
        }
    }
}

