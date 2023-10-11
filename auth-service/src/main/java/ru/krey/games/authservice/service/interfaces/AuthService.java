package ru.krey.games.authservice.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.krey.games.authservice.dto.AuthDto;

@Service
public interface AuthService {

    ResponseEntity<?> createAuthTokenAndAuthorized(AuthDto authDto);

    ResponseEntity<?> createAuthToken(AuthDto authDto);
}
