package se.yrgo.game.constants;

public enum Difficulty {
    EASY(210),
    MEDIUM(150),
    HARD(130);

    public final int obstacleGap;

    Difficulty(int obstacleGap) {
        this.obstacleGap = obstacleGap;
    }
}
