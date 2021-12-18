package game.services;

import game.models.Game;
import game.models.Round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyGameServiceImpl implements IGameService{

    Game game;
    Round round;

    public DummyGameServiceImpl(){
        this.game = new Game();
        this.round = new Round();
    }

    @Override
    public Game add() {
        Game game = new Game();
        this.game = game;
        return game;
    }

    @Override
    public List<Game> all() {
        List<Game> games = new ArrayList<>();
        games.add(game);
        return games;
    }

    @Override
    public Game getGame(int gameId) {
        if(game.getGameId() == gameId){
            return game;
        }
        return null;
    }

    @Override
    public List<Round> getRoundsForId(int gameId) {
        List<Round> rounds = new ArrayList<>();
        for(int i = 0; i < rounds.size(); i ++) {
            if (rounds.get(i).getGameId() == gameId) {
                rounds.add(round);
            }
        }

        if (rounds.size() == 0) return null;

        return rounds;
    }

    @Override
    public Round addGuess(Round round, Game game, int id) {
        this.game = game;
        this.round = round;
        return round;

    }

    @Override
    public Game playGame(int id) {
        return null;
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

    public String calculateGuess(String generated, String guess) {
        String result = "";

        if (result.equals(guess)) {
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

    public boolean isCorrect(String result) {
        if (result.equals("e:e:e:e")) return true;
        return false;
    }
}
