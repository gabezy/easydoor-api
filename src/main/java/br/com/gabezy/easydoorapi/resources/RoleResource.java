package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.PermissionDTO;
import br.com.gabezy.easydoorapi.resources.dto.RoleDTO;
import br.com.gabezy.easydoorapi.services.PermissionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for permission and role management
 */
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {

    private final PermissionService permissionService;

    public RoleResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GET
    @Path("/permissions")
    @RolesAllowed("ADMIN")
    public Response getAllPermissions() {
        try {
            List<PermissionDTO> permissions = permissionService.getAllPermissions();
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
    public Response getPermissionById(@PathParam("id") Long permissionId) {
        try {
            PermissionDTO permission = permissionService.getPermissionById(permissionId);
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
    public Response getAllRoles() {
        try {
            List<RoleDTO> roles = permissionService.getAllRoles();
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
    public Response getRoleById(@PathParam("id") Long roleId) {
        try {
            RoleDTO role = permissionService.getRoleById(roleId);
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
    public Response createRole(@QueryParam("name") String name,
                               @QueryParam("description") String description) {
        try {
            if (name == null || name.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Role name is required\"}")
                        .build();
            }
            RoleDTO role = permissionService.createRole(name, description);
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
    public Response addPermissionToRole(@PathParam("id") Long roleId,
                                        @PathParam("permissionCode") String permissionCode) {
        try {
            permissionService.addPermissionToRole(roleId, permissionCode);
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
    public Response removePermissionFromRole(@PathParam("id") Long roleId,
                                             @PathParam("permissionCode") String permissionCode) {
        try {
            permissionService.removePermissionFromRole(roleId, permissionCode);
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

