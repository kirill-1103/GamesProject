package ru.krey.games.handler.auth;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.krey.games.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthFailureHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        Map<String, String> result = new HashMap<>();

//        log.error(SecurityContextHolder.getContext().toString());

        if (exception instanceof BadCredentialsException) {
            result.put("message", "Invalid credentials");
        } else if (exception instanceof InsufficientAuthenticationException) {
            result.put("message", "Invalid authentication request");
        } else {
            result.put("message","Authentication failure");
        }
        JsonUtils.write(response.getWriter(),result);
        log.debug(String.format("Не удалось выполнить запрос {%s} (ошибка аутентификации). Ex: %s",request.getRequestURI(),exception.getMessage()));
    }
}
