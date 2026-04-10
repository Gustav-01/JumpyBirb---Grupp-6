package se.yrgo.game.data;

import java.sql.Connection;

public interface GameDatabase {
    /**
     * Initiates a database connection for the {@code GameDatabase} instance.
     * Call before trying to access the database.
     */
    void connect();

    /**
     * Get a {@link Connection} instance to access the database.
     * @return a Connection instance of the implementing database layer.
     */
    Connection getConnection();

    /**
     * Call to safely close the connection to the database.
     */
    void close();
}
