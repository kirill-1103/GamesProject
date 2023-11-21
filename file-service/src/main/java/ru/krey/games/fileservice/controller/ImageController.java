package ru.krey.games.fileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.fileservice.dto.ImageDto;
import ru.krey.games.fileservice.dto.responses.ImageResponse;
import ru.krey.games.fileservice.service.interfaces.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file/img")
public class ImageController {
    private final ImageService imageService;
    @PostMapping
    public ResponseEntity<ImageResponse> saveImg(@ModelAttribute ImageDto imageDto) throws IOException {
        return imageService.savePlayerImage(imageDto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ImageResponse> base64ByName(@PathVariable String name) throws IOException {
        return imageService.getImageBase64(name);
    }

    @PostMapping("/images")
    public ResponseEntity<List<ImageResponse>> base64ByNames(@RequestBody List<String> names){
        if(Objects.isNull(names) || names.isEmpty()){
            return ResponseEntity.ok().build();
        }
        return imageService.getImagesBase64(names);
    }

}
