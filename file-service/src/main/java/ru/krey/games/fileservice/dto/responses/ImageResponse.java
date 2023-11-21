package ru.krey.games.fileservice.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
    String imageName;
    String base64;
    boolean error = false;
    String errorMessage;
}
