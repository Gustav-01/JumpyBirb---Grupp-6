package se.yrgo.game.data;

import se.yrgo.game.constants.Difficulty;
import se.yrgo.game.data.sql.SqlConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO implementation for saving and loading scores for the game.
 * This class uses a JDBC-based database connection to store scores
 * together with the selected difficulty level.
 */
public class BirbScoreDao implements ScoreDao {

    private Connection connection;
    private GameDatabase gameDatabase = new GameDatabaseJdbc();
    ;

    /**
     * Creates a new DAO object and opens a database connection.
     */
    public BirbScoreDao() {
        gameDatabase.connect();
        connection = gameDatabase.getConnection();
    }

    /**
     * Saves a score for a specific difficulty level
     * @param score the score to save
     * @param difficulty the difficulty level the score is achieved on
     * @return true if the score was saved, otherwise false
     */
    @Override
    public boolean saveScore(int score, Difficulty difficulty) {
        try (PreparedStatement ps = connection.prepareStatement(SqlConstants.INSERT_SCORE_DIFFICULTY)) {
            ps.setInt(1, score);
            ps.setString(2, difficulty.toString());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Retrieves the highest score for the given difficulty level
     * @param difficulty the difficulty level to search for
     * @return the highest score found for that difficulty
     */
    @Override
    public int getHighscoreForDifficulty(Difficulty difficulty) {
        try (PreparedStatement ps = connection.prepareStatement(SqlConstants.SELECT_HIGHEST_SCORE_FOR_DIFFICULTY)) {
            ps.setString(1, difficulty.toString());
            var res = ps.executeQuery();
            return res.getInt(SqlConstants.FLD_SCORE);
        } catch (SQLException e) {
            throw new RuntimeException("There was an error when attempting to retrieve a highscore.", e);
        }
    }

    /**
     * Close resource connection to database
     */
    @Override
    public void close() {
        gameDatabase.close();
    }
}
