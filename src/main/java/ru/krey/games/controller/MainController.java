package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("/")
@RequiredArgsConstructor
public class MainController {


    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @GetMapping(value = {"/main","/css/*","/me**", "/rating**","/chat**","/game_list**","/player_list**",
            "/error", "/login**","/auth", "/register", "/logout","/registration","/game/**",
            "/ws**","/websocket**","/ws/**","/websocket/**","/topic/**"})
    public String main(HttpServletRequest request){
        String header = request.getHeader("referer");
            return "index";
    }

//    @RequestMapping(value = "/**",method = {RequestMethod.GET, RequestMethod.POST})
//    public @ResponseBody ResponseEntity<String> defaultPath(){
//        return new ResponseEntity<>("Unmapped request", HttpStatus.OK);
//    }


//    @RequestMapping(value =  "/{path:^(.*socket)}/**",method = {RequestMethod.GET,RequestMethod.POST})
//    public String redirect(@PathVariable String path) {
//        // Forward to home page so that route is preserved.
//        log.debug("redirect from " + path);
//        return "forward:/main";
//    }
}
