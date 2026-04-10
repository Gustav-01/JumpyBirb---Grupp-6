package se.yrgo.game.data;

import se.yrgo.game.constants.Level;
import se.yrgo.game.data.sql.SqlConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BirbScoreDao implements ScoreDao{
    private Connection connection;
    private GameDatabase gameDatabase = new GameDatabaseJdbc();;

    public BirbScoreDao() {
        gameDatabase.connect();
        connection = gameDatabase.getConnection();
    }

    @Override
    public boolean saveScore(int score) {
        try (PreparedStatement ps = connection.prepareStatement(SqlConstants.INSERT_SCORE)) {
            ps.setInt(1, score);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean saveScore(int score, Level level) {
        throw new RuntimeException("No implementation.");
    }

    @Override
    public int getHighscore() {
        try (PreparedStatement ps = connection.prepareStatement(SqlConstants.SELECT_HIGHEST_SCORE)) {
            var res = ps.executeQuery();
            return res.getInt(SqlConstants.FLD_SCORE);
        } catch (SQLException e) {
            throw new RuntimeException("There was an error when attempting to retrieve a highscore.", e);
        }
    }

    @Override
    public void close() {
        gameDatabase.close();
    }
}
