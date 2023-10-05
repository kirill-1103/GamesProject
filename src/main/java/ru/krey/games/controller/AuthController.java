package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.AuthDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.service.AuthService;
import ru.krey.games.utils.RoleUtils;
import ru.krey.games.service.interfaces.ImageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final PlayerDao playerDao;

    private final ImageService imageService;

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthDto authDto){
        return authService.createAuthTokenAndAuthorized(authDto);
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

        Player player = Player.builder()
                        .login(login)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .lastSignInTime(LocalDateTime.now())
                        .signUpTime(LocalDateTime.now())
                        .enabled(true)
                        .rating(0)
                        .Role(RoleUtils.ROLE_USER)
                        .build();


        if (playerLoginAlreadyExists(player.getLogin())) {
            throw new BadRequestException("Пользователь с таким логином уже существует.");
        }
        if (playerEmailAlreadyExists(player.getEmail())) {
            throw new BadRequestException("Пользователь с таким email уже существует.");
        }

        player = playerDao.saveOrUpdate(player);

        if(!Objects.isNull(image)){
            try{
                imageService.savePlayerImage(image,player.getId());
            }catch (IOException e){
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.",e);
            }
        }
        player.setPassword(null);
        return player;
    }

    private boolean playerLoginAlreadyExists(String login) {
        return playerDao.getOneByLogin(login).isPresent();
    }

    private boolean playerEmailAlreadyExists(String email) {
        return playerDao.getOneByEmail(email).isPresent();
    }
}
