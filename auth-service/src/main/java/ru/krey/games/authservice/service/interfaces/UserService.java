package ru.krey.games.authservice.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService extends UserDetailsService {

    ResponseEntity<?> createUser(MultipartFile image, String login, String email, String password);

}
