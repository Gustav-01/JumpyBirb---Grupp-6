package se.yrgo.game.data;

import se.yrgo.game.constants.Difficulty;

/**
 * Defines operations for saving and retrieving game scores.
 * Implementations decide how the data is stored.
 */
public interface ScoreDao {
    /**
     * Saves a score for the given difficulty level.
     *
     * @param score      the score to save
     * @param difficulty the difficulty the score was achieved on
     * @return true if the score was saved successfully
     */
    boolean saveScore(int score, Difficulty difficulty);

    /**
     * Returns the highest score stored for the given difficulty.
     *
     * @param difficulty the difficulty to search for
     * @return the highest score found
     */
    int getHighscoreForDifficulty(Difficulty difficulty);

    /**
     * Closes any open resources used by the DAO.
     * Should be called when the application shuts down.
     */
    void close();
}
