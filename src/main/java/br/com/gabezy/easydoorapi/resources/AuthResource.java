package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.LoginRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RefreshTokenRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RegisterRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.TokenResponseDTO;
import br.com.gabezy.easydoorapi.services.AuthService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authenticationService;

    public AuthResource(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequestDTO request) {
        try {
            TokenResponseDTO response = authenticationService.register(request);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Registration failed\"}")
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO request) {
        try {
            TokenResponseDTO response = authenticationService.login(request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Login failed\"}")
                    .build();
        }
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(RefreshTokenRequestDTO request) {
        try {
            if (request.refreshToken == null || request.refreshToken.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Refresh token is required\"}")
                        .build();
            }

            TokenResponseDTO response = authenticationService.refreshToken(request.refreshToken);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Token refresh failed\"}")
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(RefreshTokenRequestDTO request) {
        try {
            authenticationService.logout(request.refreshToken);
            return Response.ok("{\"message\":\"Logged out successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Logout failed\"}")
                    .build();
        }
    }
}

