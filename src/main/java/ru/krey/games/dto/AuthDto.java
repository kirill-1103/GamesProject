package ru.krey.games.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthDto {
    private  String login;
    private  String password;
    public boolean isInvalid(){
        return StringUtils.isAnyBlank(login,password);
    }
}
