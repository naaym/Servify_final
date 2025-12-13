package com.servify.shared.exception;

import com.servify.provider.exceptions.EmailDuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(EmailDuplicationException.class)
    public ResponseEntity<?> handleEmailDuplication(EmailDuplicationException exception) {
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleAuthenticationErrors(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

}
