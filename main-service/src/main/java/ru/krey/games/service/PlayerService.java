package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.mapper.PlayerMapper;
import ru.krey.games.openapi.ApiException;
import ru.krey.games.openapi.ApiResponse;
import ru.krey.games.openapi.api.PlayerApi;
import ru.krey.games.openapi.model.PlayerOpenApi;
import ru.krey.games.utils.http.HttpStatusChecker;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerApi playerApi;

    private final PlayerMapper playerMapper;

    public List<Player> getActivePlayers() {

        try {
            return playerMapper.toPlayer(playerApi.getActivePlayers());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getOneById(Long id) {
        try {
            ApiResponse<PlayerOpenApi> response =playerApi.getByIdWithHttpInfo(id);
            if(response.getStatusCode() == HttpStatus.NOT_FOUND.value()){
                throw new NotFoundException("Игрока с таким id не существует!");
            }
            return playerMapper.toPlayer(response.getData());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getOneByIdOrNull(Long id){
        try {
            ApiResponse<PlayerOpenApi> response =playerApi.getByIdWithHttpInfo(id);
            if(response.getStatusCode() == HttpStatus.NOT_FOUND.value()){
                return null;
            }
            return playerMapper.toPlayer(response.getData());
        } catch (ApiException e) {
            return null;
        }
    }

    public Optional<Player> getOneByIdOpt(Long id){
        try {
            ApiResponse<PlayerOpenApi> response =playerApi.getByIdWithHttpInfo(id);
            if(response.getStatusCode() == HttpStatus.NOT_FOUND.value()){
                return Optional.empty();
            }
            return Optional.of(playerMapper.toPlayer(response.getData()));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Player update(Player player){
        try{
            ApiResponse<PlayerOpenApi> response =
                    playerApi.savePlayerWithHttpInfo(playerMapper.toOpenApi(player));
            if(!HttpStatusChecker.isSuccessful(response.getStatusCode())){
                throw new RuntimeException();
            }
            return playerMapper.toPlayer(response.getData());
        }catch (ApiException e){
            throw new RuntimeException(e);
        }
    }


    public Map<Long, PlayerOpenApi> getPlayersByIds(Set<Long> playersIds) {
        try {
            Map<String,PlayerOpenApi> map =  playerApi.getPlayersByIds(new ArrayList<>(playersIds));
            Map<Long, PlayerOpenApi> resultMap = new HashMap<>();
            map.forEach((key, value) -> resultMap.put(Long.valueOf(key), value));
            return resultMap;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
