package com.servify.shared.api;

import com.servify.provider.exceptions.EmailDuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(EmailDuplicationException.class)
    public ResponseEntity<?> handleEmailDuplication(EmailDuplicationException exception) {
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
