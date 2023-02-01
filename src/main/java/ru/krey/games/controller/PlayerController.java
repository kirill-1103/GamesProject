package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.LocalImageService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerDao playerDao;
    private final AuthService authService;

    private final LocalImageService localImageService;

    private final static Logger log = LoggerFactory.getLogger(PlayerController.class);

    @GetMapping("/{id}")
    public @ResponseBody Player getOneById(long id) {
        return playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
    }

    @GetMapping("/authenticated")
    public @ResponseBody Player getAuthenticatedUser() {
        return playerDao.getOneByLogin(authService.getCurrentUsername())
                .orElseThrow(() -> new BadRequestException("Авторизованного игрока нет!"));
    }

    @PostMapping(value = "/image")
    public @ResponseBody String getImageBase64( @RequestParam("img_name") String imgName) {
        try{
            return localImageService.getImageBase64(imgName);
        }catch (IOException e){
            log.error(e.getMessage());
            throw new BadRequestException("Такой файл не найден.",e);
        }
    }

}
