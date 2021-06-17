package game.models;
import java.time.LocalDate;

public class Round {

    private int gameId;
    private String guess;
    private String result;
    // make date a String so it's easy to handle
    // since we are displaying. The value is assigned once a row is created via SQL (table schema).
    private String guessTime;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {

        // ensure user input is of valid length and value
        if(guess.length() != 4 || !isNumeric(guess)) {
            this.guess = "0000";
        } else {
            this.guess = guess;
        }

    }

    public String getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(String guessTime) {
        this.guessTime = guessTime;
    }

    public  boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
