package ru.krey.games.authservice.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.authservice.exception.BadRequestException;
import ru.krey.games.authservice.exception.SecurityAuthenticationException;
import ru.krey.games.authservice.service.interfaces.UserService;
import ru.krey.games.authservice.utils.mapper.UserDetailsMapper;
import ru.krey.games.openapi.api.PlayerApi;
import ru.krey.games.openapi.model.PlayerOpenApi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@JsonDeserialize
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PlayerApi playerApi;
    private final UserDetailsMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return mapper.toUserDetails(playerApi.getByLogin(username));
        } catch (Exception e) {
            throw new SecurityAuthenticationException("Cannot load user by username.", e);
        }
    }

    @Override
    public ResponseEntity<?> createUser(MultipartFile image, String login, String email, String password) {
        PlayerOpenApi playerOpenApi = new PlayerOpenApi();
        playerOpenApi.setLogin(login);
        playerOpenApi.setEmail(email);
        playerOpenApi.setPassword(password);
        try {
            Path path = null;
            if (!Objects.isNull(image)) {
                path = Paths.get("./", image.getOriginalFilename());
                image.transferTo(path);
            }
            if (Objects.nonNull(path)) {
                playerApi.createPlayer(playerOpenApi, path.toFile());
            } else {
                playerApi.createPlayer(playerOpenApi, null);
            }
            return ResponseEntity.ok().build();
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Не удалось создать игрока");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> registrationHttpEntity(MultipartFile image, String login, String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("login", login);
        map.add("image", image);
        map.add("email", email);
        map.add("password", password);

        return new HttpEntity<>(map, headers);
    }
}
