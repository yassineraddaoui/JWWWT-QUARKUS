package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.LoginDto;
import org.acme.dto.RegisterDto;
import org.acme.dto.TokenResponse;
import org.acme.service.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthResource {

    @Inject
    UserService userService;


    @POST
    @Path("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user and returns a JWT token.")
    @APIResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @APIResponse(responseCode = "400", description = "Invalid input or user already exists")
    public Response signUp(@Valid RegisterDto registerData) {
        var token = userService.register(registerData);
        return Response.ok().entity(token).build();
    }

    @POST
    @Path("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @APIResponse(responseCode = "200", description = "Successfully logged in",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @APIResponse(responseCode = "401", description = "Invalid username or password")
    public Response signIn(@Valid LoginDto loginData) {
        var token = userService.signIn(loginData);
        return Response.ok().entity(token).build();
    }
}
