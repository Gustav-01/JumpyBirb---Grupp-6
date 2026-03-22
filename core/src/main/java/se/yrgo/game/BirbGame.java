package se.yrgo.game;

import com.badlogic.gdx.Game;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(gameScreen);
    }

    public void gameOver(){
        setScreen(gameOverScreen);
    }
}
