package game.tests;

import game.models.Game;
import game.models.Round;
import game.services.DummyGameServiceImpl;
import game.services.GameServiceImpl;
import game.services.IGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameDaoTest {
    IGameService dummyGameService;
    Round eRound;
    Round pRound;

    @BeforeEach
    void setUp(){
        dummyGameService = new DummyGameServiceImpl();
    }

    @Test
    public void testAddGame()
    {
        //

        // Arrange
        String answer = "1234";
        String status = "in progress";
        Game game = new Game();

        game.setGameId(1);
        game.setAnswer(answer);
        game.setStatus("in progress");
        Game addedGame = dummyGameService.add();

        // ACT & ASSERT

        assertTrue(addedGame.getGameId() == 1);
        assertTrue(addedGame.getAnswer().equalsIgnoreCase(answer));
        assertTrue(addedGame.getGameId() == 1);
        assertTrue(addedGame.getStatus().equalsIgnoreCase(status));
    }

    @Test
    public void testGetGame()
    {
        // Arrange
        String answer = "1234";
        String status = "in progress";
        Game game = new Game();

        game.setGameId(1);
        game.setAnswer(answer);
        game.setStatus("in progress");


        Game game2 = dummyGameService.getGame(game.getGameId());

        // ACT & ASSERT

        assertTrue(game2.getStatus().equalsIgnoreCase(game.getStatus()));

    }

    @Test
    public void testGenerateAnswer() {

        // TODO test no duplicates
        DummyGameServiceImpl dummyService = new DummyGameServiceImpl();
        // Arrange
        String answer = dummyService.generateAnswer();

        // Act
        assertTrue(answer.length() == 4);
        //        assertFalse(answer.length() != 4);

    }

    @Test
    public void testCalculateGuess() {
        // Arrange
        DummyGameServiceImpl dummyService = new DummyGameServiceImpl();
        String guess = dummyService.calculateGuess("1234","1234");
        //Act
        assertTrue(guess.equalsIgnoreCase("e:e:e:e"));
    }

    @Test
    public void testValidInput() {
        // Arrange
        DummyGameServiceImpl dummyService = new DummyGameServiceImpl();
        boolean input = dummyService.isCorrect("123k");
        //Act
        assertFalse(input);

    }
}
