package ru.krey.games.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.krey.games.handler.auth.AuthFailureHandler;

@Controller("/")
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping(value={
            "/","/login**","/register","/temp","/temp/*","/auth","/css/*"
    })
    public String main(){
        return "index";
    }
}
