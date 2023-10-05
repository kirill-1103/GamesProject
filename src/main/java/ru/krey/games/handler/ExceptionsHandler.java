package ru.krey.games.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.krey.games.domain.ExceptionResponse;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.error.SecurityAuthenticationException;


@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(SecurityAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthException(SecurityAuthenticationException e){
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e){
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
}
