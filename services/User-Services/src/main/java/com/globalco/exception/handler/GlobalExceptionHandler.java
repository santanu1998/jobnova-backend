package com.globalco.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.globalco.exceptions.EmailAlreadyExistsException;
import com.globalco.exceptions.InvalidOperationException;
import com.globalco.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Wrong password or unknown email.
     * <p>
     * Both return the same message on purpose: telling the caller which one was
     * wrong lets an attacker discover which email addresses are registered.
     */
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentials(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Invalid email or password")
                .error("Unauthorized")
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .error("Email Already Exists")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .error("Invalid Operation")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .error("Invalid Argument")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Resource not found: " + e.getResourcePath())
                .error("Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Bean-validation failures on @Valid request bodies.
     * <p>
     * This has to be declared explicitly: the catch-all below would otherwise
     * take precedence over the framework default and turn a 400 into a 500.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(details.isBlank() ? "Request validation failed" : details)
                .error("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Last resort, so an unforeseen failure still returns a readable body.
     * <p>
     * Without this the caller gets a bare Spring error page with no message at
     * all, which is indistinguishable from the service being down. The cause is
     * logged in full here but deliberately not sent to the client.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("Unhandled exception in user-service", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Something went wrong on our side. Please try again.")
                .error(e.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Body that Jackson cannot bind - malformed JSON, or a value that is not a
     * member of an enum such as an unknown role. That is bad input from the
     * caller, so it is a 400; without this it falls through to the catch-all
     * and is reported as a server fault.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Request body is malformed or contains an unsupported value")
                .error("Malformed Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
