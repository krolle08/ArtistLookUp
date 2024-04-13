package Application.controller;

import Application.utils.LoggingUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public static ResponseEntity<String> handleException(Exception e) {
        LoggingUtility.error("An unexpected error occurred: ", e);
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public static ResponseEntity<String> handleRuntimeException(RuntimeException e) {
         LoggingUtility.error("A runtime exception occurred: ", e);
         e.printStackTrace();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("A runtime exception occurred: " + e.getMessage());
    }
}
