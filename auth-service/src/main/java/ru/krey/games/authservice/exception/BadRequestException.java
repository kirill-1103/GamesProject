package ru.krey.games.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Exception cause) {
        super(message, cause);
    }

    public BadRequestException() {
        super();
    }

    public BadRequestException(Exception cause) {
        super(cause);
    }
}
