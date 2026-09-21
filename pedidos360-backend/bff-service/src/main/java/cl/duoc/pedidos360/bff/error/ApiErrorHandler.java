package cl.duoc.pedidos360.bff.error;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(RestClientException.class)
    ResponseEntity<Map<String, Object>> unavailable(RestClientException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
            "timestamp", Instant.now().toString(), "status", 502,
            "error", "Bad Gateway", "message", "Microservicio temporalmente no disponible"));
    }
}
