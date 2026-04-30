package se.yrgo.game.data.sql;

/**
 * For simplicity, to keep all SQL at one place.
 */
public class SqlConstants {
    public static final String TABLE_NAME = "highscore";
    public static final String FLD_SCORE = "score";
    public static final String FLD_DIFFICULTY = "difficulty";

    public static final String CREATE_TABLE = String.format("""
        CREATE TABLE IF NOT EXISTS %s (
        id INTEGER PRIMARY KEY,
        %s INTEGER NOT NULL,
        %s TEXT
        )""", TABLE_NAME, FLD_SCORE, FLD_DIFFICULTY);

    public static final String INSERT_SCORE_DIFFICULTY = String.format("""
        INSERT INTO %s (%s, %s)
        VALUES (?,?)""", TABLE_NAME, FLD_SCORE, FLD_DIFFICULTY);

    public static final String SELECT_HIGHEST_SCORE_FOR_DIFFICULTY = String.format("""
        SELECT %s
        FROM %s
        WHERE %s = ?
        ORDER BY %s DESC
        LIMIT 1""", FLD_SCORE, TABLE_NAME, FLD_DIFFICULTY, FLD_SCORE);
}
