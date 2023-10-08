package ru.krey.games.state;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.interfaces.StorableGame;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GameKeeper implements  Iterable<StorableGame> {
    Set<StorableGame> savedGames = new CopyOnWriteArraySet<>();

    public void addGame(StorableGame game) {
        savedGames.add(game);
    }

    public Optional<StorableGame> getById(Long id){
        return savedGames.stream().filter(savedGame -> savedGame.getId().equals(id))
                .findAny();
    }

    @Override
    public Iterator<StorableGame> iterator() {
        return savedGames.iterator();
    }

    public void removeById(Long id){
        savedGames.removeIf((g) -> id.equals(g.getId()));
    }

}
