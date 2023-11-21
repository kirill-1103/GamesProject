package ru.krey.libs.jwtlib.utils.state;

import org.springframework.stereotype.Component;
import ru.krey.libs.jwtlib.utils.JwtUtils;

import java.util.List;
import java.util.Objects;

@Component
public class TokenKeeper {
    private JwtUtils jwtUtils;

    private final Long timeForUpdateMs = 1000 * 60L * 10;

    private String token = null;

    public TokenKeeper(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    public String getToken(String serviceName, List<String> rolesList){
        try{
            if(Objects.isNull(token) || jwtUtils.getTimeLeftMs(token) < timeForUpdateMs){
                this.token = jwtUtils.generateToken(serviceName, rolesList);
            }
        }catch (Exception e){
            this.token = jwtUtils.generateToken(serviceName, rolesList);
        }
        return token;
    }

}
