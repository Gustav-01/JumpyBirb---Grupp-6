package se.yrgo.game.data;

import se.yrgo.game.constants.Difficulty;

public interface ScoreDao {
    boolean saveScore(int score, Difficulty difficulty);
    int getHighscoreForDifficulty(Difficulty difficulty);
    void close();
}
