package game.services;

import game.models.Game;
import game.models.Round;

import java.util.List;

public interface IGameService {

    Game add(Game game);
    List<Game> all();
    Game getGame(int gameId);
    List<Round> getRoundsForId(int gameId);
    // adds to Round , and updates Game table if game is done.
    Round addGuess(Round round, Game game, int id);

}
