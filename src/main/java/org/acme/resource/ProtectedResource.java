package org.acme.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/protected")
public class ProtectedResource {

    @GET
    @RolesAllowed("user") // Only users with the "user" role can access this endpoint
    public Response protectedEndpoint() {
        return Response.ok().entity("This is a protected endpoint").build();
    }
}
