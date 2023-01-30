package ru.krey.games.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.krey.games.config.SecurityConfig;
import ru.krey.games.error.NotFoundException;

@Controller("/")
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @GetMapping(value = {"/css/*","/me**", "/rating**","/chat**","/game_list**","/player_list**",
            "/error", "/login**","/auth", "/register", "/logout","/registration"})
    public String main(){
        return "index";
    }
}
