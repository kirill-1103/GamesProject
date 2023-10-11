package ru.krey.games.authservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.krey.games.authservice.dto.ExceptionResponse;
import ru.krey.games.authservice.exception.BadRequestException;
import ru.krey.games.authservice.exception.SecurityAuthenticationException;

import java.util.Objects;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(SecurityAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(SecurityAuthenticationException e) {
        ExceptionResponse response;
        if (Objects.nonNull(e.getCause())) {
            response = new ExceptionResponse(e.getMessage(), e.getCause().getMessage());
        } else {
            response = new ExceptionResponse(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
