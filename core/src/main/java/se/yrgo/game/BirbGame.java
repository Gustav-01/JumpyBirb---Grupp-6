package se.yrgo.game;

import com.badlogic.gdx.Game;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {

    private GameScreen gameScreen;


    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    /**
     * Is called when rendering should be performed.
     */
    @Override
    public void render() {
        super.render();

    }

}
