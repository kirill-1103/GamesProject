package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerDao playerDao;

}
