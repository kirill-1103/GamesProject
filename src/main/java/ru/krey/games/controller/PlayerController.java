package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TetrisGameDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.dto.AuthDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.LocalImageService;
import ru.krey.games.service.interfaces.ImageService;
import ru.krey.games.utils.AuthUtils;
import ru.krey.games.utils.GameUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerDao playerDao;

    private final TttGameDao tttGameDao;

    private final TetrisGameDao tetrisGameDao;
    private final AuthUtils authUtils;

    private final ImageService imageService;

    private final LocalImageService localImageService;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final Environment env;

    private final AuthService authService;



    private final static Logger log = LoggerFactory.getLogger(PlayerController.class);

    @GetMapping("/{id}")
    public Player getOneById(@PathVariable Long id) {
        Player player = playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
        player.setPassword(null);
        return player;
    }

    @GetMapping("/authenticated")
    public Player getAuthenticatedUser(Principal principal) {
        if(principal == null || principal.getName() == null){
            throw new BadRequestException("Авторизованного игрока нет!");
        }
        Player player = playerDao.getOneByLogin(principal.getName())
                .orElseThrow(() -> new BadRequestException("Авторизованного игрока нет!"));
        player.setPassword(null);
        return player;
    }

    @PostMapping(value = "/image")
    public String getImageBase64(@RequestParam("img_name") String imgName) {
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

    @PostMapping("/images")
    public List<String> getImagesBase64ByImagesNames(@RequestBody List<String> names) {
        List<String> images = new ArrayList<>();
        if (names == null || names.size() == 0) {
            throw new BadRequestException("Список имет пуст");
        }
        names.forEach((name) -> {
            if (name == null || name.isBlank()) {
                images.add(null);
                return;
            }
            try {
                images.add(localImageService.getImageBase64(name));
            } catch (IOException e) {
                log.error(e.getMessage());
                try {
                    images.add(localImageService.getImageBase64(env.getProperty("player.image.default")));
                } catch (IOException ex) {
                    throw new BadRequestException("Дефолтная картинка не найдена.", e);
                }
            }
        });
        return images;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePlayer(
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
        if (playerWithSameLogin != null && !Objects.equals(playerWithSameLogin.getId(), id)) {
            throw new BadRequestException("Пользователь с таким логином уже существует!");
        }

        Player playerWithSameEmail = playerDao.getOneByEmail(email).orElse(null);
        if (playerWithSameEmail != null && !Objects.equals(playerWithSameEmail.getId(), id)) {
            throw new BadRequestException("Пользователь с таким email уже существует!");
        }

        playerFromDb.setLogin(login);
        playerFromDb.setEmail(email);

        if (password != null && !password.isBlank()) {
            playerFromDb.setPassword(bCryptPasswordEncoder.encode(password));
        }

        Player player = playerDao.saveOrUpdate(playerFromDb);
        playerFromDb.setPassword(null);

        if (img != null && !img.isEmpty()) {
            try {
                imageService.savePlayerImage(img, playerFromDb.getId());
            } catch (IOException e) {
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.", e);
            }
        }
        player.setPassword(null);
        return authService.createAuthToken(AuthDto.builder().login(login).password(password).build());
    }

    @PostMapping("/currentGameCode")
    public Integer getCurrentGameCode(@RequestParam("id") Long playerId) {
        Player player = playerDao.getOneById(playerId).orElseThrow(() -> new NotFoundException("Игрока с таким id нет!"));
        return player.getLastGameCode();
    }

    @PostMapping("/currentGameId")
    public Long getCurrentGameId(@RequestParam("id") Long playerId) {
        Player player = playerDao.getOneById(playerId).orElseThrow(() -> new NotFoundException("Игрока с таким id нет!"));
        if (player.getLastGameCode() == null) {
            return null;
        }

        Game game = null;


        if (player.getLastGameCode() == GameUtils.TTT_GAME_CODE) {
            game = tttGameDao.getCurrentGameByPlayerId(playerId)
                    .orElse(null);
        }

        if(player.getLastGameCode() == GameUtils.TETRIS_GAME_CODE){
            game = tetrisGameDao.getCurrentGameByPlayerId(playerId)
                    .orElse(null);
        }

        return game == null ? null : game.getId();
    }

    @PostMapping("/rating")
    public List<Player> getAllOrderedByRatingStepByStep(@RequestParam Long from, @RequestParam Long to) {

        List<Player> players = playerDao.getAllOrderByRating();
        if (from >= players.size()) {
            return new ArrayList<>();
        }
        return players.subList(Math.min(players.size() - 1, from.intValue()), Math.min(players.size(), to.intValue()));
    }

    @GetMapping("/top/{id}")
    public Long getPlayerTop(@PathVariable Long id) {
        return playerDao.getPlayerTopById(id);
    }

    @PostMapping("/search")
    public List<Player> getSearchResult(@RequestParam("search") String search, @RequestParam("from") int from, @RequestParam("to") int to) {
        if (from > to) {
            throw new BadRequestException("from>to");
        }
        log.info("Search:" + search);
        if (search.isBlank()) {
            return new ArrayList<>();
        }
        search = search.trim();

        List<Player> result = new ArrayList<>();

        Consumer<Player> addIfNotContains = (player) -> {
            if (!result.contains(player)) {
                result.add(player);
            }
        };

        playerDao.getOneByLogin(search).ifPresent(addIfNotContains);
        playerDao.getOneByEmail(search).ifPresent(addIfNotContains);

        playerDao.getPlayersWithNameStarts(search.toLowerCase()).forEach(addIfNotContains);
        playerDao.getPlayersByPartOfName(search.toLowerCase()).forEach(addIfNotContains);
        playerDao.getPlayersByPartOfEmail(search.toLowerCase()).forEach(addIfNotContains);

        if (result.isEmpty()){
            return result;
        }
        return result.subList(Math.min(result.size() - 1, from), Math.min(result.size(), to));
    }

}
