package game.services;

import game.data.GameDAO;
import game.models.Game;
import game.models.Round;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImpl implements IGameService{

    GameDAO dao;

    public GameServiceImpl(GameDAO dao) {
        this.dao = dao;
    }

    @Override
    public Game add() {
        Game game = new Game();
        game.setAnswer(generateAnswer());
        game.setStatus("in progress");
        return dao.add(game);
    }

    @Override
    public List<Game> all() {
        return dao.getAll();
    }

    @Override
    public Game getGame(int gameId) {
        return dao.findById(gameId);
    }

    @Override
    public List<Round> getRoundsForId(int gameId) {
        return dao.findRoundsById(gameId);
    }

    @Override
    public Round addGuess(Round round, Game game, int id) {
        try {
            game = dao.findGameToPlay(id);
            round.setResult(calculateGuess(game.getAnswer(), round.getGuess()));

            // update status if game is finished (guess is correct)
            if (isCorrect(calculateGuess(game.getAnswer(), round.getGuess()))){
                game.setStatus("finished");
                dao.updateGameStatus(game);
            }

            return dao.makeGuess(round, id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Game playGame(int id) {
        return dao.findGameToPlay(id);
    }

    // helper method to generate random, not duplicate numbers
    public String generateAnswer() {
        // create a list to hold numbers 0-9
        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            numbers.add(i);
        }

        // shuffle
        Collections.shuffle(numbers);

        String asnwer = "";
        for(int i = 0; i < 4; i++){
            asnwer += numbers.get(i).toString();
        }
        return asnwer;
    }

    public static String calculateGuess(String generated, String guess) {
        String result = "";
        int exact = 0;
        int partial = 0;

        if (generated.equals(guess)) {
            result = "e:4:p:0";
            return result;
        } else {
            for (int i = 0; i < guess.length(); i++) {
                if (generated.charAt(i) == guess.charAt(i)) {
                    exact ++;
                } else if (generated.contains(guess.charAt(i) + "")) {
                    partial ++;
                }
            }
            result = "e:" + exact + ":p:" + partial;
            return result;
        }
    }

    public boolean isCorrect(String guess) {
        if (guess.equals("e:4:p:0")) return true;
        return false;
    }

}
