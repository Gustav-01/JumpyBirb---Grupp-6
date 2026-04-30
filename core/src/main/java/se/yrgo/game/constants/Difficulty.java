package se.yrgo.game.constants;

public enum Difficulty {
    EASY(210, "Easy"),
    MEDIUM(150, "Medium"),
    HARD(130, "Hard");

    public final int obstacleGap;
    public final String prettyName;

    Difficulty(int obstacleGap, String prettyName) {
        this.obstacleGap = obstacleGap;
        this.prettyName = prettyName;
    }

}
