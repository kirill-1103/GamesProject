package ru.krey.games.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.authservice.dto.AuthDto;
import ru.krey.games.authservice.exception.BadRequestException;
import ru.krey.games.authservice.service.AuthServiceImpl;
import ru.krey.games.authservice.service.UserServiceImpl;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    private static final String MAPPING_AUTH = "/auth";
    private static final String MAPPING_UPDATE_TOKEN = "/updateToken";
    private static final String MAPPING_REGISTRATION = "/registration";

    @PostMapping(MAPPING_AUTH)
    public ResponseEntity<?> createToken(@RequestBody AuthDto authDto){
        return authService.createAuthTokenAndAuthorized(authDto);
    }

    @PostMapping(MAPPING_UPDATE_TOKEN)
    public ResponseEntity<?> updateToken(Principal principal){
        return authService.createAuthToken(AuthDto.builder().login(principal.getName()).build());
    }

    @PostMapping(MAPPING_REGISTRATION)
    public ResponseEntity<?> createPlayer(@RequestParam(value = "player_img", required = false) MultipartFile image,
                                   @RequestParam("login") String login,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password){
        if (login == null || login.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }

        return userService.createUser(image, login, email, password);
    }
}
