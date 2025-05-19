package kz.saya.finals.mainservice.Controllers;

import kz.saya.finals.common.DTOs.Game.GameCreateDTO;
import kz.saya.finals.common.DTOs.Game.GameDTO;
import kz.saya.finals.mainservice.Entities.Game;
import kz.saya.finals.mainservice.Mappers.GameMapper;
import kz.saya.finals.mainservice.Services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/public/all")
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames().stream().map(GameMapper::toDTO).toList();
        return ResponseEntity.ok(games);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createGame(@RequestBody GameCreateDTO gameCreateDTO) {
        try {
            gameService.createGame(gameCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Game created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable UUID id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(GameMapper.toDTO(game));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}


