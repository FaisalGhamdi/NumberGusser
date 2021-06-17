package game.controllers;

import game.data.GameDAO;
import game.models.Game;
import game.models.Round;
import game.services.GameServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody Game game) {
        return gameService.add(game);
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guess(@RequestBody Round round, Game game) {
        return gameService.addGuess(round, game);
    }

    @GetMapping("/rounds/{id}")
    public List<Round>  findRoundsById(@PathVariable int id) {
        return gameService.getRoundsForId(id);
    }

//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ToDo create(@RequestBody ToDo todo) {
//        return dao.add(todo);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ToDo> findById(@PathVariable int id) {
//        ToDo result = dao.findById(id);
//        if (result == null) {
//            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
//        }
//        return ResponseEntity.ok(result);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity update(@PathVariable int id, @RequestBody ToDo todo) {
//        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
//        if (id != todo.getId()) {
//            response = new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
//        } else if (dao.update(todo)) {
//            response = new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//        return response;
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity delete(@PathVariable int id) {
//        if (dao.deleteById(id)) {
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }
}
