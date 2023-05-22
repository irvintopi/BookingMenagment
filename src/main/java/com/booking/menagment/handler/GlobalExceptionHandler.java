package com.booking.menagment.handler;

import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex){
        return ResponseEntity.badRequest().body("Check your input, missing required data!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleSqlException(SQLException s) {
        return ResponseEntity.ok().body("All okay!:)");
    }

}
