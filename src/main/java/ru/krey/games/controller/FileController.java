package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    @PostMapping("/player_image")
    public void temp(@RequestParam("image") MultipartFile image,
                     @RequestParam(value = "player_id",required = false) Long player_id){
        return;
    }
}
