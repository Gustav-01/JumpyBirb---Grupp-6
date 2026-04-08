package se.yrgo.game;

import se.yrgo.game.data.ScoreDao;

public class HighscoreService {
    private ScoreDao scoreDao;

    public void registerFinalScore(int score) {
        storeHighscore(score);
    }

    public void storeHighscore(int score) {
        try {
            scoreDao.saveScore(score);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getPreviousHighscore()  {
        try {
            return scoreDao.getHighscore();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public void dispose() {
        scoreDao.close();
    }

}
