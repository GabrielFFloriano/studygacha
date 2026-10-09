package com.gacha.studygacha.exceptions;

import com.gacha.studygacha.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {UsernameAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "USERNAME_ALREADY_EXISTS",
                e.getMessage(),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
