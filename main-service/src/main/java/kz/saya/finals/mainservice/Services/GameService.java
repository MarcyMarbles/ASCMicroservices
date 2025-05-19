package kz.saya.finals.mainservice.Services;

import kz.saya.finals.common.DTOs.Game.GameCreateDTO;
import kz.saya.finals.mainservice.Entities.Game;
import kz.saya.finals.mainservice.Repositories.GameRepository;
import kz.saya.sbasecore.Entity.FileDescriptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getAllGamesByGenre(String genre) {
        return gameRepository.findAllByGenre(genre);
    }

    public Game getGameById(UUID id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game getGameByName(String name) {
        return gameRepository.findGameByName(name);
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    public void deleteGame(UUID id) {
        gameRepository.deleteById(id);
    }

    public void deleteGame(Game game) {
        gameRepository.delete(game);
    }

    public void deleteAllGames() {
        gameRepository.deleteAll();
    }

    public void createGame(GameCreateDTO game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        Game newGame = new Game();
        newGame.setName(game.getName());
        newGame.setDescription(game.getDescription());
        newGame.setGenre(game.getGenre());
        if (game.getImage() != null) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setFileData(game.getImage());
            newGame.setImage(fileDescriptor);
        }
        newGame.setOfficialSite(game.getOfficialSite());
        saveGame(newGame);
    }

}
