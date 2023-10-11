package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.service.LocalImageService;
import ru.krey.games.service.PlayerService;
import ru.krey.games.service.interfaces.ImageService;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
@Slf4j
public class PlayerController {
    private final ImageService imageService;

    private final LocalImageService localImageService;

    private final PlayerService playerService;

    @GetMapping("/{id}")
    public Player getOneById(@PathVariable Long id) {
        return playerService.getOneById(id);
    }

    @GetMapping("/login/{login}")
    public Player getOneByLogin(@PathVariable String login) {
        return playerService.getOneByLogin(login);
    }

    @GetMapping("/authenticated")
    public Player getAuthenticatedUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new BadRequestException("Авторизованного игрока нет!");
        }
        return playerService.getOneByLogin(principal.getName());
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
        if (names == null || names.size() == 0) {
            throw new BadRequestException("Список имет пуст");
        }
        return imageService.getImagesBase64(names);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePlayer(
            @RequestParam("login") String login,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("id") Long id,
            @RequestParam(value = "player_img", required = false) MultipartFile img
    ) {
//        if (login == null || login.isBlank() ||
//                email == null || email.isBlank()) {
//            throw new BadRequestException("Не все поля заполнены.");
//        }
//        Player player = playerService.updatePlayer(login, email, password, id, img);
//        return authService.createAuthToken(AuthDto.builder().login(player.getLogin()).build());
        return null;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createPlayer(@RequestParam(value = "player_img", required = false) MultipartFile image,
                                          @RequestParam("login") String login,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password
    ) {
        if (login == null || login.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }


        playerService.createPlayer(image, login, email, password);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/currentGameCode")
    public Integer getCurrentGameCode(@RequestParam("id") Long playerId) {
        return playerService.getOneById(playerId).getLastGameCode();
    }

    @PostMapping("/currentGameId")
    public Long getCurrentGameId(@RequestParam("id") Long playerId) {
        return playerService.getCurrentGame(playerId)
                .map(Game::getId)
                .orElse(null);
    }

    @PostMapping("/rating")
    public List<Player> getAllOrderedByRatingStepByStep(@RequestParam Long from, @RequestParam Long to) {
        return playerService.getPartOrderedByRating(from, to);
    }

    @GetMapping("/top/{id}")
    public Long getPlayerTop(@PathVariable Long id) {
        return playerService.getPlayerTopById(id);
    }

    @PostMapping("/search")
    public List<Player> getSearchResult(@RequestParam("search") String search, @RequestParam("from") int from, @RequestParam("to") int to) {
        if (from > to) {
            throw new BadRequestException("from>to");
        }
        if (search.isBlank()) {
            return new ArrayList<>();
        }
        search = search.trim();

        return playerService.search(search, from, to);
    }

    @PostMapping("/update-active")
    public void updateActive(Principal principal) {
        if (Objects.nonNull(principal)) {
            playerService.updateActive(principal.getName());
        }
    }
}
