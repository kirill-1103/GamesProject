package ru.krey.games.fileservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDto {
    private MultipartFile img;
    private String login;
    private Long id;
}
