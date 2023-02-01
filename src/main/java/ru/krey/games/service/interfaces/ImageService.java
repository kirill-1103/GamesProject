package ru.krey.games.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {
    //saving file and add img identifier for player
    void savePlayerImage(MultipartFile img, Long playerId) throws IOException;

    //get img by img identifier
    String getImageBase64(String name) throws IOException, FileNotFoundException;
}
