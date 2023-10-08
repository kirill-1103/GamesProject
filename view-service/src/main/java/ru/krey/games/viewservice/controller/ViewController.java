package ru.krey.games.viewservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class ViewController {
    @GetMapping(value = "/**")
    public String view(){
        return "index";
    }
}
