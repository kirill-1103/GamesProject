package ru.krey.games.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class SecurityAuthenticationException extends RuntimeException {
    public SecurityAuthenticationException() {
        super();
    }

    public SecurityAuthenticationException(String message) {
        super(message);
    }

    public SecurityAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
