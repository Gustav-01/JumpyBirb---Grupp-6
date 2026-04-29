package se.yrgo.game.constants;

public enum Difficulty {
    EASY(210),
    INTERMEDIATE(180),
    HARD(150);

    public final int obstacleGap;

    Difficulty(int obstacleGap) {
        this.obstacleGap = obstacleGap;
    }
}
