package ru.krey.games.utils.http;

import org.springframework.http.HttpStatus;

public class HttpStatusChecker {
    public static boolean isSuccessful(int status){
        return status < HttpStatus.MULTIPLE_CHOICES.value() &&  status >= HttpStatus.OK.value();
    }
}
