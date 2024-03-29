package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.interfaces.ImageService;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LocalImageService implements ImageService {
    public static final int max_size = 50000000;
    private final Environment env;

    private final PlayerService playerService;

    private final static Logger log = LoggerFactory.getLogger(LocalImageService.class);

    @Override
    public void savePlayerImage(MultipartFile img, Long playerId) throws IOException {
        File uploadDir = getUploadDir();
        Player player = playerService.getOneById(playerId);

        LocalDateTime time = LocalDateTime.now();
        String fileName = "img_" + time.getYear() + "_" + time.getMonth() + "_" + time.getDayOfMonth()
                + "_" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond() + "_"
                + player.getLogin() + "_" + player.getId() + "_"
                + img.getOriginalFilename().toLowerCase().replaceAll(" ", "_");


        if(!(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".bmp"))){
            throw new BadRequestException("Файл должен иметь один из следующих форматов: .jpeg, .jpg, .png, .bmp! Пользователь зарегистрирован, попробуйте изменить изображение в профиле.");
        }

        File fileToSave = (new File(uploadDir.getAbsolutePath() + "/" + fileName));
        log.debug(fileToSave.toString());
        img.transferTo(fileToSave);
        player.setPhoto(fileName);
        Player playerFromDb = playerService.update(player);
        if(playerFromDb.getPhoto()==null){
            throw new BadRequestException("Не удалось загрузить фото!");
        }
    }

    @Override
    public String getImageBase64(String imgName) throws IOException, FileNotFoundException {
        File uploadDir = getUploadDir();
        String name = uploadDir.getAbsolutePath() + "/" + imgName;

        File imgFile = new File(uploadDir.getAbsolutePath() + "/" + imgName);
        if(!imgFile.exists()){
            throw new FileNotFoundException("Файл не найден");
        }
        byte[] fileContent = FileUtils.readFileToByteArray(imgFile);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    @Override
    public List<String> getImagesBase64(List<String> names) throws RuntimeException {
        List<String> images = new ArrayList<>();
        names.forEach((name) -> {
            if (name == null || name.isBlank()) {
                images.add(null);
                return;
            }
            try {
                images.add(getImageBase64(name));
            } catch (IOException e) {
                log.error(e.getMessage());
                try {
                    images.add(getImageBase64(env.getProperty("player.image.default")));
                } catch (IOException ex) {
                    throw new RuntimeException("Дефолтная картинка не найдена.", e);
                }
            }
        });
        return images;
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
