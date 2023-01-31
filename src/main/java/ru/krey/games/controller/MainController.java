package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.krey.games.config.SecurityConfig;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.AuthService;

@Controller("/")
@RequiredArgsConstructor
public class MainController {


    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @GetMapping(value = {"/css/*","/me**", "/rating**","/chat**","/game_list**","/player_list**",
            "/error", "/login**","/auth", "/register", "/logout","/registration"})
    public String main(Model model){
        return "index";
    }
}
