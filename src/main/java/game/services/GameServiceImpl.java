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
    public Game add(Game game) {
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
    public Round addGuess(Round round, Game game) {
        game = dao.findInProgressGame();
        round.setResult(calculateGuess(game.getAnswer(), round.getGuess()));

        // update status ig game is finished (guess is correct)
        if (isCorrect(calculateGuess(game.getAnswer(), round.getGuess()))){
            game.setStatus("finished");
            dao.updateGameStatus(game);
        }

        return dao.makeGuess(round);
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
        // bug. empty
        String result = "";

        // TODO: chnage to to cols - check generated & guess

        if (generated.equals(guess)) {
            result = "e:e:e:e";
            return result.substring(0,result.length()-1);
        } else {
            for (int i = 0; i < guess.length(); i++) {
                if (generated.charAt(i) == guess.charAt(i)) {
                    result += "e:";
                } else if (generated.contains(guess.charAt(i) + "")) {
                    result += "p:";
                } else {
                    result += "0:";
                }
            }
            // remove last colin and return
            return result.substring(0,result.length()-1);
        }
    }

    public boolean isCorrect(String guess) {
        if (guess.equals("e:e:e:e")) return true;
        return false;
    }

}
