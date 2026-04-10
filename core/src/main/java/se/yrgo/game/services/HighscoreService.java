package se.yrgo.game.services;

import se.yrgo.game.data.BirbScoreDao;
import se.yrgo.game.data.ScoreDao;

public class HighscoreService {
    private ScoreDao scoreDao;

    public HighscoreService() {
        this.scoreDao = new BirbScoreDao();
    }

    public boolean registerFinalScore(int score) {
        if (isNewHighscore(score)) {
            try {
                scoreDao.saveScore(score);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private boolean isNewHighscore(int score) {
        return getPreviousHighscore() < score;
    }

//    private boolean storeHighscore(int score) {
//        try {
//            ;
//        } catch (RuntimeException e) {
//            System.err.println(e.getMessage());
//        }
//    }

    public int getPreviousHighscore() {
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
