package ru.krey.games.domain;

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

    public ExceptionResponse(String message){
        this.message = message;
    }
}
