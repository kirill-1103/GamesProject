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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.authservice.constant.ServicesNames;
import ru.krey.games.authservice.domain.UserDetailsImpl;
import ru.krey.games.authservice.exception.SecurityAuthenticationException;
import ru.krey.games.authservice.service.interfaces.UserService;

@Service
@JsonDeserialize
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final static String URL_CREATE_PLAYER = String.format("http://%s/api/player/new", ServicesNames.MAIN_SERVICE);
    private final static String URL_GET_USER = String.format("http://%s/api/player/login/{login}", ServicesNames.MAIN_SERVICE);

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return restTemplate.getForObject(URL_GET_USER, UserDetailsImpl.class, username);
        } catch (Exception e) {
            throw new SecurityAuthenticationException("Cannot load user by username.", e);
        }
    }

    @Override
    public ResponseEntity<?> createUser(MultipartFile image, String login, String email, String password) {

        return restTemplate.postForEntity(URL_CREATE_PLAYER,
                registrationHttpEntity(image, login, email, password),
                Object.class);
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
