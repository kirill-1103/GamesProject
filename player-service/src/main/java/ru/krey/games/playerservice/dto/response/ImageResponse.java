package ru.krey.games.playerservice.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageResponse {
    String imageName;
    String base64;
    boolean error = false;
    String errorMessage;
}
