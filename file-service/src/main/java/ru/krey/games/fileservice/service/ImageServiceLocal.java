package ru.krey.games.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.krey.games.fileservice.dto.ImageDto;
import ru.krey.games.fileservice.dto.responses.ImageResponse;
import ru.krey.games.fileservice.service.interfaces.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageServiceLocal implements ImageService {

    public static final int IMG_MAX_SIZE = 50000000;

    private final Environment env;

    @Override
    public ResponseEntity<ImageResponse> savePlayerImage(ImageDto imageDto) throws IOException {
        if (Objects.isNull(imageDto.getImg())) {
            log.debug("img is null");
            return getExceptionResponseEntity("Передано пустое изображение.");
        }
        File uploadDir = getUploadDir();
        String fileName = getFileName(imageDto);

        if (!(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp"))) {
            log.debug("Файл не  загружен. Формат неверный.");
            return getExceptionResponseEntity("Файл должен иметь один из следующих форматов: .jpeg, .jpg, .png, .bmp!");
        }
        File fileToSave = (new File(uploadDir.getAbsolutePath() + "/" + fileName));

        log.debug(fileToSave.toString());

        imageDto.getImg().transferTo(fileToSave);

        return ResponseEntity.ok(ImageResponse.builder().imageName(fileName).build());
    }

    @Override
    public ResponseEntity<ImageResponse> getImageBase64(String imgName) {
        try {
            File uploadDir = getUploadDir();

            String name = uploadDir.getAbsolutePath() + "/" + imgName;

            File imgFile = new File(name);
            if (!imgFile.exists()) {
                log.debug(String.format("File %s not found",imgName));
                return getExceptionResponseEntity("Файл не найден");
            }
            byte[] fileContent = FileUtils.readFileToByteArray(imgFile);
            return ResponseEntity.ok(ImageResponse.builder()
                    .base64(Base64.getEncoder().encodeToString(fileContent))
                    .build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ImageResponse.builder().error(true).errorMessage(e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<List<ImageResponse>> getImagesBase64(List<String> names) throws RuntimeException {
        List<ImageResponse> images = new ArrayList<>();
        names.forEach((name) -> {
            if (name == null || name.isBlank()) {
                images.add(null);
                return;
            }
            ResponseEntity<ImageResponse> imgResponse = getImageBase64(name);
            if(imgResponse.getStatusCode() != HttpStatus.OK){
                images.add(getImageBase64(env.getProperty("player.image.default")).getBody());
            }else{
                images.add(imgResponse.getBody());
            }

        });
        return ResponseEntity.ok(images);
    }

    private ResponseEntity<ImageResponse> getExceptionResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ImageResponse.builder().error(true).errorMessage(message)
                        .build());
    }

    private String getFileName(ImageDto imageDto) {
        LocalDateTime time = LocalDateTime.now();
        return "img_" + time.getYear() + "_" + time.getMonth() + "_" + time.getDayOfMonth()
                + "_" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond() + "_"
                + imageDto.getLogin() + "_" + imageDto.getId() + "_"
                + imageDto.getImg().getOriginalFilename().toLowerCase().replaceAll(" ", "_");
    }

    private File getUploadDir() throws IOException {
        String uploadPath = env.getProperty("player.image.upload.path");
        if (uploadPath == null) {
            log.error("Не получилось получить uploadPath из app properties");
            throw new IOException("Не удалось получить путь загрузки");
        }
        File uploadDir = Paths.get(uploadPath).toAbsolutePath().toFile();
        if (!uploadDir.exists()) {
            boolean result = uploadDir.mkdirs();
            if (!result) {
                log.error("Не удалось создать директорию для изображений");
                throw new IOException("Не удалось создать директорию");
            }
        }
        return uploadDir;
    }
}
