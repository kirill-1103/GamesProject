package ru.krey.games.playerservice.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveImage(MultipartFile img, String login, Long id) throws IOException;
}
