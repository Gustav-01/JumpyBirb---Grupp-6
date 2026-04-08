package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private HighscoreService highscoreService;

    private int previousHighscore;

    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        highscoreService = new HighscoreService();
        previousHighscore = highscoreService.getPreviousHighscore();

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    public void gameOver(int score){
        gameOverScreen = new GameOverScreen(this, score);
        setScreen(gameOverScreen);
        highscoreService.registerFinalScore(score);

        gameScreen.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        highscoreService.dispose();
    }

    public int getPreviousHighscore() {
        return previousHighscore;
    }
}
