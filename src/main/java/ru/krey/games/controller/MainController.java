package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @GetMapping(value = {"/main","/css/*","/me**", "/rating**","/chat/**","/game_list**","/player_list**",
            "/error", "/login**","/auth", "/register", "/logout","/registration","/game/**",
            "/ws**","/websocket**","/ws/**","/websocket/**","/topic/**","/player/**","/api/chat/**","/api/tetris_game/**","/favicon.ico","/auth/**","/auth","/",
            "/updateToken"})
    public String main(){
        return "index";
    }
}
