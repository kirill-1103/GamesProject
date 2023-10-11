package ru.krey.games.authservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthDto {
    private String login;
    private String password;

    public boolean isInvalid() {
        return login.isBlank() || password.isBlank();
    }
}
