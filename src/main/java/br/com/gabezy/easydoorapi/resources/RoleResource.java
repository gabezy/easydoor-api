package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.PermissionDTO;
import br.com.gabezy.easydoorapi.resources.dto.RoleDTO;
import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.SimpleMessageResponseDTO;
import br.com.gabezy.easydoorapi.services.RoleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

/**
 * REST Resource for permission and role management
 */
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Roles", description = "Role and permission management endpoints")
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @GET
    @Path("/permissions")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "List permissions", description = "Returns all permissions. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Permissions returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PermissionDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view permissions"),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching permissions",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getAllPermissions() {
        try {
            List<PermissionDTO> permissions = roleService.getAllPermissions();
            return Response.ok(permissions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch permissions\"}")
                    .build();
        }
    }

    @GET
    @Path("/permissions/{id}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get permission by id", description = "Returns a single permission by identifier. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Permission returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PermissionDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view permissions"),
            @APIResponse(responseCode = "404", description = "Permission not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching permission",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getPermissionById(@PathParam("id") Long permissionId) {
        try {
            PermissionDTO permission = roleService.getPermissionById(permissionId);
            if (permission == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Permission not found\"}")
                        .build();
            }
            return Response.ok(permission).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch permission\"}")
                    .build();
        }
    }

    @GET
    @Operation(summary = "List roles", description = "Returns all roles. Public endpoint.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Roles returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = RoleDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching roles",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getAllRoles() {
        try {
            List<RoleDTO> roles = roleService.getAllRoles();
            return Response.ok(roles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch roles\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get role by id", description = "Returns a single role by identifier. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Role returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RoleDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to view roles"),
            @APIResponse(responseCode = "404", description = "Role not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected error while fetching role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response getRoleById(@PathParam("id") Long roleId) {
        try {
            RoleDTO role = roleService.getRoleById(roleId);
            if (role == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Role not found\"}")
                        .build();
            }
            return Response.ok(role).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch role\"}")
                    .build();
        }
    }

    /**
     * Create new role (requires ADMIN role)
     *
     * @param name the role name
     * @param description the role description
     * @return Response with created role
     */
    @POST
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create a role", description = "Creates a new role. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Role created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RoleDTO.class))),
            @APIResponse(responseCode = "400", description = "Role name is required",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to create roles"),
            @APIResponse(responseCode = "409", description = "Role already exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected error while creating role",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response createRole(@Parameter(description = "Role name", example = "SUPERVISOR") @QueryParam("name") String name,
                               @Parameter(description = "Role description", example = "Supervisor role") @QueryParam("description") String description) {
        try {
            if (name == null || name.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Role name is required\"}")
                        .build();
            }
            RoleDTO role = roleService.createRole(name, description);
            return Response.status(Response.Status.CREATED).entity(role).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create role\"}")
                    .build();
        }
    }

    /**
     * Add permission to role (requires ADMIN role)
     *
     * @param roleId the role ID
     * @param permissionCode the permission code
     * @return Response
     */
    @POST
    @Path("/{id}/permissions/{permissionCode}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add permission to role", description = "Adds a permission to a role. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Permission added successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleMessageResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "Role or permission is invalid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to update roles"),
            @APIResponse(responseCode = "500", description = "Unexpected error while adding permission",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response addPermissionToRole(@PathParam("id") Long roleId,
                                        @PathParam("permissionCode") String permissionCode) {
        try {
            roleService.addPermissionToRole(roleId, permissionCode);
            return Response.ok("{\"message\":\"Permission added to role successfully\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to add permission to role\"}")
                    .build();
        }
    }

    /**
     * Remove permission from role (requires ADMIN role)
     *
     * @param roleId the role ID
     * @param permissionCode the permission code
     * @return Response
     */
    @DELETE
    @Path("/{id}/permissions/{permissionCode}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Remove permission from role", description = "Removes a permission from a role. Access is restricted to ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Permission removed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleMessageResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "Role or permission is invalid",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Authentication required"),
            @APIResponse(responseCode = "403", description = "User does not have permission to update roles"),
            @APIResponse(responseCode = "500", description = "Unexpected error while removing permission",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response removePermissionFromRole(@PathParam("id") Long roleId,
                                             @PathParam("permissionCode") String permissionCode) {
        try {
            roleService.removePermissionFromRole(roleId, permissionCode);
            return Response.ok("{\"message\":\"Permission removed from role successfully\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to remove permission from role\"}")
                    .build();
        }
    }
}
