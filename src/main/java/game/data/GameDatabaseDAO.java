package game.data;

import game.models.Game;
import game.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
@Repository
public class GameDatabaseDAO implements GameDAO{

    // TODO: fix requarmets chnagw

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) {

        final String sql = "INSERT INTO game(answer, status) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, game.getAnswer());
            statement.setString(2, game.getStatus());
            return statement;

        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {
        final String sql =
                "SELECT gameid" +
                ", case when status = \"in progress\" then \"Game in Progress - Can't show the answer!\"" +
                " else answer end as answer" +
                ", status FROM game;";

        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int id) {
        try {
            final String sql =
                    "SELECT gameid" +
                            ", case when status = \"in progress\" then \"Game in Progress - Can't show the answer!\"" +
                            " else answer end as answer" +
                            ", status FROM game" +
                            " WHERE gameid = (?);";
            return jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Round> findRoundsById(int id) {
        final String sql =
                "SELECT *" +
                        " FROM Round" +
                        " WHERE gameid = (?)" +
                        " ORDER BY guessTime ;";

        return jdbcTemplate.query(sql, new RoundMapper(), id);
    }

    // Hlper method to get the current in progress game
    // TODO: change


    // updates game status to 'finished'
    @Override
    public void updateGameStatus(Game game) {
        final String sql =
                "UPDATE Game SET "
                + "status = ? "
                + "WHERE gameid = ?;";

         jdbcTemplate.update(sql, game.getStatus(), game.getGameId());
    }

    @Override
    public Round makeGuess(Round round, int id) {
        Game currGame = findGameToPlay(id);

        if (currGame == null) {
            return null;
        }

        final String sql = "INSERT INTO round(gameId, guess, result) VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, currGame.getGameId() + "");
            statement.setString(2, round.getGuess());
            statement.setString(3, round.getResult());
            return statement;

        }, keyHolder);

        round.setGameId(currGame.getGameId());

        return round;
    }

    @Override
    public Game findGameToPlay(int id) {
        try {
            final String sql =
                    "SELECT * FROM gamedb.game WHERE status <> \"finished\" AND gameId = (?);";
            return jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setStatus(rs.getString("status"));
            return game;
        }
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setGameId(rs.getInt("gameId"));
            round.setGuess(rs.getString("guess"));
            round.setResult(rs.getString("result"));
            round.setGuessTime(rs.getString("guessTime"));
            return round;
        }
    }
}
