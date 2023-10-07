package ru.krey.games.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ImageService {
    //saving file and add img identifier for player
    void savePlayerImage(MultipartFile img, Long playerId) throws IOException;

    //get img by img identifier
    String getImageBase64(String name) throws IOException, FileNotFoundException;

    List<String> getImagesBase64(List<String> names) throws RuntimeException;
}
