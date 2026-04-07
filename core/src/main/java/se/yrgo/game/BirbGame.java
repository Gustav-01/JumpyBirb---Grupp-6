package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


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
        setScreen(gameScreen);
    }

    public void gameOver(int score){
        gameOverScreen = new GameOverScreen(this, score);
        setScreen(gameOverScreen);
        gameScreen.dispose();
    }
}
