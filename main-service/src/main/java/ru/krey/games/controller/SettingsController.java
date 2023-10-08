package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krey.games.service.LocalImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController {
    @GetMapping("/img_size")
    public int getSize(){
        return LocalImageService.max_size;
    }
}
