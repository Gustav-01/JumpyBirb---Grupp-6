package se.yrgo.game.data.sql;

/**
 * For simplicity, to keep all SQL at one place.
 */
public class SqlConstants {
    private static String TABLE_NAME = "highscore";
    private static String FLD_SCORE = "score";
    private static String FLD_DIFFICULTY = "difficulty";

    public static final String CREATE_TABLE = String.format("""
        CREATE TABLE IF NOT EXISTS %s (
        id INTEGER PRIMARY KEY,
        %s INTEGER NOT NULL,
        %s TEXT
        )""", TABLE_NAME, FLD_SCORE, FLD_DIFFICULTY);

    public static final String INSERT_SCORE = String.format("""
        INSERT INTO %s (%s)
        VALUES (?)""", TABLE_NAME, FLD_SCORE);

    public static final String INSERT_SCORE_DIFFICULTY = String.format("""
        INSERT INTO %s (%s, %s)
        VALUES (?,?)""", TABLE_NAME, FLD_SCORE, FLD_DIFFICULTY);
}
