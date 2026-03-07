package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.PermissionDTO;
import br.com.gabezy.easydoorapi.resources.dto.RoleDTO;
import br.com.gabezy.easydoorapi.services.PermissionApplicationService;
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

    private final PermissionApplicationService permissionApplicationService;

    public RoleResource(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    /**
     * Get all permissions (requires ADMIN role)
     *
     * @return Response with list of permissions
     */
    @GET
    @Path("/permissions")
    @RolesAllowed("ADMIN")
    public Response getAllPermissions() {
        try {
            List<PermissionDTO> permissions = permissionApplicationService.getAllPermissions();
            return Response.ok(permissions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch permissions\"}")
                    .build();
        }
    }

    /**
     * Get permission by ID (requires ADMIN role)
     *
     * @param permissionId the permission ID
     * @return Response with permission data
     */
    @GET
    @Path("/permissions/{id}")
    @RolesAllowed("ADMIN")
    public Response getPermissionById(@PathParam("id") Long permissionId) {
        try {
            PermissionDTO permission = permissionApplicationService.getPermissionById(permissionId);
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

    /**
     * Create new permission (requires ADMIN role)
     *
     * @param permissionCode the permission code
     * @param description the permission description
     * @return Response with created permission
     */
    @POST
    @Path("/permissions")
    @RolesAllowed("ADMIN")
    public Response createPermission(@QueryParam("code") String permissionCode,
                                     @QueryParam("description") String description) {
        try {
            if (permissionCode == null || permissionCode.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Permission code is required\"}")
                        .build();
            }
            PermissionDTO permission = permissionApplicationService.createPermission(permissionCode, description);
            return Response.status(Response.Status.CREATED).entity(permission).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to create permission\"}")
                    .build();
        }
    }

    /**
     * Get all roles (requires ADMIN role)
     *
     * @return Response with list of roles
     */
    @GET
    public Response getAllRoles() {
        try {
            List<RoleDTO> roles = permissionApplicationService.getAllRoles();
            return Response.ok(roles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to fetch roles\"}")
                    .build();
        }
    }

    /**
     * Get role by ID (requires ADMIN role)
     *
     * @param roleId the role ID
     * @return Response with role data
     */
    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response getRoleById(@PathParam("id") Long roleId) {
        try {
            RoleDTO role = permissionApplicationService.getRoleById(roleId);
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
            RoleDTO role = permissionApplicationService.createRole(name, description);
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
            permissionApplicationService.addPermissionToRole(roleId, permissionCode);
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
            permissionApplicationService.removePermissionFromRole(roleId, permissionCode);
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

