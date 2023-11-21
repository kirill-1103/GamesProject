package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.playerservice.constant.ServicesNames;
import ru.krey.games.playerservice.dto.response.ImageResponse;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.service.interfaces.FileService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final RestTemplate restTemplate;

    private final static String SAVE_IMG_URL = String.format("http://%s/api/file/img", ServicesNames.FILE_SERVICE);

    @Override
    public String saveImage(MultipartFile img, String login, Long id) throws IOException {
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("login", login);
        requestMap.add("id", id);

        HttpEntity<?> httpEntity = httpEntityWithFiles(requestMap, List.of(img), List.of("img"));

        ResponseEntity<ImageResponse> response;
        try {
            response =
                    restTemplate.postForEntity(SAVE_IMG_URL, httpEntity, ImageResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("Не удалось загрузить изображение. Попробуйте позже в профиле.");
        }
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new BadRequestException(response.getBody().getErrorMessage());
        }
        return response.getBody().getImageName();
    }

    private HttpEntity<?> httpEntityWithFiles(MultiValueMap<String, Object> requestMap,
                                              List<MultipartFile> files, List<String> keys) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        for (int i = 0; i < files.size(); i++) {
            ByteArrayResource byteArrayResource = byteArrayResourceFromMultipartFile(files.get(i));
            requestMap.add(keys.get(i), byteArrayResource);
        }
        return new HttpEntity<>(requestMap, httpHeaders);
    }

    private ByteArrayResource byteArrayResourceFromMultipartFile(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
}
