package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.utils.AuthUtils;
import ru.krey.games.utils.GameUtils;
import ru.krey.games.service.LocalImageService;
import ru.krey.games.service.interfaces.ImageService;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerDao playerDao;

    private final TttGameDao tttGameDao;
    private final AuthUtils authUtils;

    private final ImageService imageService;

    private final LocalImageService localImageService;

    private final PasswordEncoder bCryptPasswordEncoder;



    private final static Logger log = LoggerFactory.getLogger(PlayerController.class);

    @GetMapping("/{id}")
    public @ResponseBody Player getOneById(@PathVariable Long id) {
        Player player = playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
        player.setPassword(null);
        return player;
    }

    @GetMapping("/authenticated")
    public @ResponseBody Player getAuthenticatedUser() {
        Player player = playerDao.getOneByLogin(authUtils.getCurrentUsername())
                .orElseThrow(() -> new BadRequestException("Авторизованного игрока нет!"));
        player.setPassword(null);
        return player;
    }

    @PostMapping(value = "/image")
    public @ResponseBody String getImageBase64(@RequestParam("img_name") String imgName) {
        if (imgName == null || imgName.isBlank()) {
            throw new BadRequestException("Имя файла пустое");
        }
        try {
            return localImageService.getImageBase64(imgName);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Такой файл не найден.", e);
        }
    }

    @PostMapping("/update")
    public @ResponseBody Player updatePlayer(
            @RequestParam("login") String login,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("id") Long id,
            @RequestParam(value = "player_img", required = false) MultipartFile img
    ) {
        if (login == null || login.isBlank() ||
                email == null || email.isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }
        Player playerFromDb = playerDao.getOneById(id)
                .orElseThrow(() -> new BadRequestException("Нет пользователя с таким id."));

        Player playerWithSameLogin = playerDao.getOneByLogin(login).orElse(null);
        if(playerWithSameLogin != null && !Objects.equals(playerWithSameLogin.getId(), id)){
            throw new BadRequestException("Пользователь с таким логином уже существует!");
        }

        Player playerWithSameEmail = playerDao.getOneByEmail(email).orElse(null);
        if(playerWithSameEmail!=null && !Objects.equals(playerWithSameEmail.getId(),id)){
            throw new BadRequestException("Пользователь с таким email уже существует!");
        }

        playerFromDb.setLogin(login);
        playerFromDb.setEmail(email);

        if(password!=null && !password.isBlank()){
            playerFromDb.setPassword(bCryptPasswordEncoder.encode(password));
        }

        Player player = playerDao.saveOrUpdate(playerFromDb);
        playerFromDb.setPassword(null);
        player.setPassword(null);

        if(img!=null && !img.isEmpty()){
            try{
                imageService.savePlayerImage(img,playerFromDb.getId());
            }catch (IOException e){
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.",e);
            }
        }

        authUtils.changeSessionUser(player);//change username and password in session

        return player;
    }

    @PostMapping("/currentGameCode")
    @ResponseBody Integer getCurrentGameCode(@RequestParam("id") Long playerId){
        Player player = playerDao.getOneById(playerId).orElseThrow(() -> new NotFoundException("Игрока с таким id нет!"));
        return player.getLastGameCode();
    }

    @PostMapping("/currentGameId")
    @ResponseBody Long getCurrentGameId(@RequestParam("id") Long playerId){
        Player player = playerDao.getOneById(playerId).orElseThrow(() -> new NotFoundException("Игрока с таким id нет!"));
        if(player.getLastGameCode() == null){
            return null;
        }

        if(player.getLastGameCode() == GameUtils.TttGameCode){
            TttGame game =  tttGameDao.getCurrentGameByPlayerId(playerId)
                    .orElse(null);
            return game == null ? null : game.getId();
        }

        return null;
    }


}
