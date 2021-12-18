package game.controllers;

import game.models.Game;
import game.models.Round;
import game.services.GameServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private GameServiceImpl gameService;

    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> all() {
        return gameService.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findById(@PathVariable int id) {

        Game result = gameService.getGame(id);
        if (result == null) {
            return new ResponseEntity("Game not found!", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create() {
        return gameService.add();
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Round> makeGuess(@RequestBody Round round) {

        Game game = gameService.playGame(round.getGameId());
        if (game == null) {
            return new ResponseEntity("Game not found or has already been played!", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gameService.addGuess(round, game, round.getGameId()));
    }

    @GetMapping("/rounds/{id}")
    public List<Round>  findRoundsById(@PathVariable int id) {
        return gameService.getRoundsForId(id);
    }
}
