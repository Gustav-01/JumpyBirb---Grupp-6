package se.yrgo.game.data;

import se.yrgo.game.data.sql.SqlConstants;

import java.sql.*;

public class GameDatabaseJdbc implements GameDatabase {
    private static final String CONNECTION_STRING = "jdbc:sqlite:data/scores.db";

    private Connection connection;

    /**
     * Call when starting up application to connect to the database. Creates a schema if none exists.
     */
    @Override
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
            statement.execute(SqlConstants.CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error when creating game data: " + e.getMessage(), e);
        }
    }

    /**
     * Get a {@link Connection} instance to access the database.
     *
     * @return a Connection instance of the implementing database layer.
     * @throws NullPointerException if connect() has yet to be called, and no connection is established.
     */
    @Override
    public Connection getConnection() {
        if (connection == null) {
            throw new NullPointerException("Cannot access data: No database connection exists.");
        }
        return connection;
    }

    @Override
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
