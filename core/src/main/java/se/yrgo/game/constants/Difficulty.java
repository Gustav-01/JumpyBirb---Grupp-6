package se.yrgo.game.constants;

/**
 * Represents the difficulty levels of the game.
 * Each level has a gap value that changes depending on
 * the players choice. Easy, medium and hard.
 */
public enum Difficulty {
    EASY(210, "Easy"),
    MEDIUM(150, "Medium"),
    HARD(130, "Hard");

    public final int obstacleGap;
    public final String prettyName;
    /**
     * Created the difficulty level with a specifik obstacle gap
     * and name.
     * @param obstacleGap the vertical space between obstacles
     * @param prettyName the name shown to the player
     */
    Difficulty(int obstacleGap, String prettyName) {
        this.obstacleGap = obstacleGap;
        this.prettyName = prettyName;
    }

}
