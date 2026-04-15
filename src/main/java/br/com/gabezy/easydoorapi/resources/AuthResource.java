package br.com.gabezy.easydoorapi.resources;

import br.com.gabezy.easydoorapi.resources.dto.LoginRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RefreshTokenRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.RegisterRequestDTO;
import br.com.gabezy.easydoorapi.resources.dto.SimpleErrorResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.SimpleMessageResponseDTO;
import br.com.gabezy.easydoorapi.resources.dto.TokenResponseDTO;
import br.com.gabezy.easydoorapi.services.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Authentication and token lifecycle operations")
public class AuthResource {

    private final AuthService authenticationService;

    public AuthResource(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @POST
    @PermitAll
    @Path("/register/client")
    @Operation(summary = "Register a client", description = "Creates a client user account and returns the generated authentication tokens.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Client registered successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "Invalid payload or duplicated username/e-mail",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response register(@Valid RegisterRequestDTO request) {
        TokenResponseDTO response = authenticationService.registerClient(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user with username and password and returns access and refresh tokens.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponseDTO.class))),
            @APIResponse(responseCode = "400", description = "Invalid credentials",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "423", description = "User account is inactive")
    })
    public Response login(@Valid LoginRequestDTO request) {
        TokenResponseDTO response = authenticationService.login(request);
        return Response.ok(response).build();
    }

    @POST
    @Path("/refresh")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Refresh access token", description = "Returns a new access token from a valid refresh token.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TokenResponseDTO.class))),
            @APIResponse(responseCode = "401", description = "Invalid or expired refresh token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected refresh error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response refreshToken(@Valid RefreshTokenRequestDTO request) {
        try {
            TokenResponseDTO response = authenticationService.refreshToken(request.refreshToken());
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Logout", description = "Revokes the provided refresh token.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Logout completed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleMessageResponseDTO.class))),
            @APIResponse(responseCode = "500", description = "Unexpected logout error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleErrorResponseDTO.class)))
    })
    public Response logout(@Valid RefreshTokenRequestDTO request) {
        try {
            authenticationService.logout(request.refreshToken());
            return Response.ok("{\"message\":\"Logged out successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Logout failed\"}")
                    .build();
        }
    }
}
