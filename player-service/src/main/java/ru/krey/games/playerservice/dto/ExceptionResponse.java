package ru.krey.games.playerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private String debugMessage;
    private Boolean error = true;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
