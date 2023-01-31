package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.AuthService;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerDao playerDao;
    private final AuthService authService;

    @GetMapping("/{id}")
    public @ResponseBody  Player getOneById(long id){
        return playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
    }

    @GetMapping("/authenticated")
    public @ResponseBody Player getAuthenticatedUser(){
        return playerDao.getOneByLogin(authService.getCurrentUsername())
                .orElseThrow(() -> new BadRequestException("Авторизованного игрока нет!"));
    }

}
