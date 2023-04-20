package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller("/")
@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = {"/main","/css/*","/me**", "/rating**","/chat**","/game_list**","/player_list**",
            "/error", "/login**","/auth", "/register", "/logout","/registration","/game/**",
            "/ws**","/websocket**","/ws/**","/websocket/**","/topic/**","/player/**","/api/chat/**"})
    public String main(HttpServletRequest request){
        return "index";
    }
}
