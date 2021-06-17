package game.data;

import game.models.Game;
import game.models.Round;

import java.util.List;

public interface GameDAO {
    Game add(Game game);
    List<Game> getAll();
    Game findById(int id);
    List <Round> findRoundsById(int id);

    //TODO
    Round makeGuess(Round round, int id);
    Game findGameToPlay(int id);
    void updateGameStatus(Game game);

}
