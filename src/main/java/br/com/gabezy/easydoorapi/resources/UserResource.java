package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.SimpleMessageResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.UpdateUserRequest;
import br.com.gabezy.easydoorapi.resources.dto.UserDTO;
import br.com.gabezy.easydoorapi.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "JWT")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @RolesAllowed({"ADMIN", "VIEW_USERS"})
    @Operation(summary = "List users", description = "Returns all users. Access is restricted to ADMIN or VIEW_USERS.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Users returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = UserDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view users"),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching users",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch users\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "VIEW_USERS"})
    @Operation(summary = "Get user by id", description = "Returns a single user by identifier. Access is restricted to ADMIN or VIEW_USERS.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view users"),
            @APIResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching the user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getUserById(@PathParam("id") Long userId) {
        try {
            UserDTO user = userService.getUserById(userId);
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
    @Operation(summary = "Update a user", description = "Updates user e-mail and active status. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserDTO.class))),
            @APIResponse(responseCode = "400", description = "Invalid payload or duplicated e-mail",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to update users")
    })
    public Response updateUser(@PathParam("id") Long userId, @Valid UpdateUserRequest request) {
        return Response.ok(userService.updateUser(userId, request)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Deactivate a user", description = "Soft-deletes a user by deactivating it. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "User deactivated successfully"),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to delete users"),
            @APIResponse(responseCode = "500", description = "Unexpected error while deleting the user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response deleteUser(@PathParam("id") Long userId) {
        try {
            userService.deleteUser(userId);
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
    @Operation(summary = "Assign role to user", description = "Assigns a role to a user. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Role assigned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleMessageResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "User or role is invalid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to assign roles"),
            @APIResponse(responseCode = "500", description = "Unexpected error while assigning role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response assignRoleToUser(@PathParam("id") Long userId, @PathParam("roleName") String roleName) {
        try {
            userService.assignRoleToUser(userId, roleName);
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
    @Operation(summary = "Remove role from user", description = "Removes a role from a user. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Role removed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleMessageResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "User or role is invalid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to remove roles"),
            @APIResponse(responseCode = "500", description = "Unexpected error while removing role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response removeRoleFromUser(@PathParam("id") Long userId, @PathParam("roleName") String roleName) {
        try {
            userService.removeRoleFromUser(userId, roleName);
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
