package com.globalco.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turns Gemini / validation failures into a readable JSON body.
 * Without this the AI endpoints return an empty 500 and the UI can only say
 * "something went wrong", which hides the actual cause.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException e) {
        return build(HttpStatus.BAD_REQUEST, "Validation Failed",
                "Missing required parameter: " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception e) {
        String raw = e.getMessage() == null ? "" : e.getMessage();
        log.error("AI request failed: {}", raw);

        // Translate the common Gemini failures into something actionable
        if (raw.contains("API_KEY_INVALID") || raw.contains("API key not valid")) {
            return build(HttpStatus.SERVICE_UNAVAILABLE, "AI Unavailable",
                    "The AI service is not configured correctly: the Gemini API key is missing or invalid. "
                            + "Set GEMINI_API_KEY on the AI-Service and restart it.");
        }
        if (raw.contains("RESOURCE_EXHAUSTED") || raw.contains("429")) {
            return build(HttpStatus.TOO_MANY_REQUESTS, "AI Rate Limited",
                    "The Gemini API quota has been exceeded. Please try again in a moment.");
        }
        if (raw.contains("NOT_FOUND") || raw.contains("404")) {
            return build(HttpStatus.SERVICE_UNAVAILABLE, "AI Unavailable",
                    "The configured Gemini model is not available for this API key. "
                            + "Check gemini.api.model in AI-Service.");
        }
        if (raw.contains("PERMISSION_DENIED") || raw.contains("UNAUTHENTICATED")) {
            return build(HttpStatus.SERVICE_UNAVAILABLE, "AI Unavailable",
                    "Gemini rejected the credentials for this request. Check GEMINI_API_KEY.");
        }
        return build(HttpStatus.SERVICE_UNAVAILABLE, "AI Unavailable",
                "The AI service could not complete this request. Please try again.");
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("error", error);
        body.put("status", status.value());
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, status);
    }
}
