package se.yrgo.game.data;

import se.yrgo.game.constants.Level;

public interface ScoreDao {
    boolean saveScore(int score);
    boolean saveScore(int score, Level level);
    int getHighscore();
    void close();
}
