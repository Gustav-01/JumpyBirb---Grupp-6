package se.yrgo.game.services;

import se.yrgo.game.constants.Difficulty;
import se.yrgo.game.data.BirbScoreDao;
import se.yrgo.game.data.ScoreDao;

public class HighscoreService {
    private ScoreDao scoreDao;

    public HighscoreService() {
        this.scoreDao = new BirbScoreDao();
    }

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

    private boolean isNewHighscore(int score, Difficulty difficulty) {
        return getPreviousHighscore(difficulty) < score;
    }

    public int getPreviousHighscore(Difficulty difficulty) {
        try {
            return scoreDao.getHighscoreForDifficulty(difficulty);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public void dispose() {
        scoreDao.close();
    }

}
