package se.yrgo.game.data;

import java.sql.*;

public class GameDatabase {
    private static final String CONNECTION_STRING = "jdbc:sqlite:data/scores.db";
    private static final String createTableSql = """
        CREATE TABLE IF NOT EXISTS highscore (
        id INTEGER PRIMARY KEY,
        score INTEGER NOT NULL,
        difficulty TEXT
        )
        """;
    private Connection connection;

    /**
     * Call when starting up application to connect to the database. Creates a schema if none exists.
     */
    public void connect() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            initDb();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the game data storage: " + e.getMessage(), e);
        }
    }

    private void initDb() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Error when creating game data: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new NullPointerException("Cannot access data: No database connection exists.");
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close data connection correctly: " + e.getMessage(), e);
            }
        }
    }
}
