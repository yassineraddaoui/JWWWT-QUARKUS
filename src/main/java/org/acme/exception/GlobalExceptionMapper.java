package org.acme.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import io.micrometer.core.instrument.config.validate.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    @APIResponses({
            @APIResponse(responseCode = "400", description = "Bad Request"),
            @APIResponse(responseCode = "401", description = "Unauthorized"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }

    private static final Map<Class<? extends Exception>, Response.Status> EXCEPTION_STATUS_MAP = Map.of(
            IllegalArgumentException.class, Response.Status.BAD_REQUEST,
            UserAlreadyExistsException.class, Response.Status.BAD_REQUEST,
            WrongCredentialsException.class, Response.Status.UNAUTHORIZED,
            UserNotFoundException.class, Response.Status.BAD_REQUEST,
            NotFoundException.class, Response.Status.NOT_FOUND,
            ValidationException.class, Response.Status.BAD_REQUEST,
            ConstraintViolationException.class, Response.Status.BAD_REQUEST
            );

    private Response mapExceptionToResponse(Exception exception) {
        Response.Status status = EXCEPTION_STATUS_MAP.getOrDefault(exception.getClass(),
                Response.Status.INTERNAL_SERVER_ERROR);

        if (status == Response.Status.INTERNAL_SERVER_ERROR) {
            return handleInternalServerError();
        } else {
            return handleClientError(exception, status);
        }
    }

    private Response handleInternalServerError() {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("title", "Internal Server Error");
        errorResponse.put("status", String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
        errorResponse.put("message", "An unexpected error occurred. Please try again later.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }

    private Response handleClientError(Exception exception, Response.Status status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("title", status.getReasonPhrase());
        errorResponse.put("status", String.valueOf(status.getStatusCode()));
        errorResponse.put("message", exception.getMessage());

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}