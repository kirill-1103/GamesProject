package ru.krey.games.fileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krey.games.fileservice.service.ImageServiceLocal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file/settings")
public class SettingsController {
    @GetMapping("/img_size")
    public int getSize(){
        return ImageServiceLocal.IMG_MAX_SIZE;
    }
}
