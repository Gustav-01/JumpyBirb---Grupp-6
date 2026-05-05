package se.yrgo.game.services;

import se.yrgo.game.constants.Difficulty;
import se.yrgo.game.data.BirbScoreDao;
import se.yrgo.game.data.ScoreDao;

/**
 * Handles highscore logic for the game.
 * This service checks if a ascore is a new personal best
 * and saves it using the underlying {@link ScoreDao}.
 */
public class HighscoreService {
    private ScoreDao scoreDao;

    /**
     * Creates a new highscore service with a default DAO implementation.
     */
    public HighscoreService() {
        this.scoreDao = new BirbScoreDao();
    }

    /**
     * Register a final score if it is higher than the previous highscore.
     *
     * @param score      the score achieved by the player
     * @param difficulty the difficulty level the score was achieved on
     * @return true if the score was saved as a new highscore
     */
    public boolean registerFinalScore(int score, Difficulty difficulty) {
        if (isNewHighscore(score, difficulty)) {
            try {
                scoreDao.saveScore(score, difficulty);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(String.format(
                    "Error when saving score of %d points at difficulty %s", score, difficulty.prettyName), e);
            }
        }
        return false;
    }

    /**
     * Checks if given score is a new highscore
     *
     * @param score      the score to compare
     * @param difficulty the difficulty level
     * @return true if the score is a new highscore
     */
    private boolean isNewHighscore(int score, Difficulty difficulty) {
        return getPreviousHighscore(difficulty) < score;
    }

    /**
     * Returns the stored highscore for the given difficulty
     * @param difficulty the difficulty level
     * @return the highest score found
     */
    public int getPreviousHighscore(Difficulty difficulty) {
        try {
            return scoreDao.getHighscoreForDifficulty(difficulty);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    /**
     * Disposes resources used by the underlying DAO.
     */
    public void dispose() {
        scoreDao.close();
    }

}
