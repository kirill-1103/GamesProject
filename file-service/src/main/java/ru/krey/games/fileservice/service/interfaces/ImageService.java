package ru.krey.games.fileservice.service.interfaces;

import org.springframework.http.ResponseEntity;
import ru.krey.games.fileservice.dto.ImageDto;
import ru.krey.games.fileservice.dto.responses.ImageResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ImageService {
    ResponseEntity<ImageResponse> savePlayerImage(ImageDto imgDto) throws IOException;

    ResponseEntity<ImageResponse> getImageBase64(String name) throws IOException, FileNotFoundException;

    ResponseEntity<List<ImageResponse>> getImagesBase64(List<String> names) throws RuntimeException;
}
