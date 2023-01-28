package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.service.RoleService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class AuthController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PlayerDao playerDao;

    @PostMapping("/registration")
    public void createPlayer(@RequestBody Player player){
        if(player.getLogin() == null || player.getLogin().isBlank() ||
                player.getEmail() == null || player.getEmail().isBlank() ||
                player.getPassword() == null || player.getPassword().isBlank()){
            throw new BadRequestException("Не все поля заполнены.");
        }
        player.setLastSignInTime(LocalDateTime.now());
        player.setSignUpTime(LocalDateTime.now());
        player.setEnabled(true);
        player.setRating(0);
        player.setRole(RoleService.ROLE_USER);
        player.setPassword(bCryptPasswordEncoder.encode(player.getPassword()));

        if(playerLoginAlreadyExists(player.getLogin())){
            throw new BadRequestException("Пользователь с таким логином уже существует.");
        }
        if(playerEmailAlreadyExists(player.getEmail())){
            throw new BadRequestException("Пользователь с таким email уже существует.");
        }
        playerDao.saveOrUpdate(player);
    }

    private boolean playerLoginAlreadyExists(String login){
        return playerDao.getOneByLogin(login).isPresent();
    }

    private boolean playerEmailAlreadyExists(String email){
        return playerDao.getOneByEmail(email).isPresent();
    }
}
