package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.AuthDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.PlayerService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final PlayerService playerService;
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthDto authDto){
        return authService.createAuthTokenAndAuthorized(authDto);
    }

    @PostMapping("/updateToken")
    public ResponseEntity<?> updateToken(Principal principal){
        return authService.createAuthToken(AuthDto.builder().login(principal.getName()).build());
    }

    @PostMapping("/registration")
    public Player createPlayer(@RequestParam(value = "player_img", required = false) MultipartFile image,
                               @RequestParam("login") String login,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password
    ) {

        if (login == null || login.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }

        return playerService.createPlayer(image, login, email, password);
    }

}
