package se.yrgo.game.data;

import se.yrgo.game.constants.Difficulty;

public interface ScoreDao {
    boolean saveScore(int score);
    boolean saveScore(int score, Difficulty difficulty);
    int getHighscore();
    int getHighscoreForDifficulty(Difficulty difficulty);
    void close();
}
