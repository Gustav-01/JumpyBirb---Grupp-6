package se.yrgo.game;

import com.badlogic.gdx.Game;
import se.yrgo.game.screens.GameOverScreen;
import se.yrgo.game.screens.GameScreen;
import se.yrgo.game.services.HighscoreService;


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

    public void saveScore(int score) {
        highscoreService.registerFinalScore(score);
    }

    @Override
    public void dispose() {
        super.dispose();
        highscoreService.dispose();
    }

    public int getPreviousHighscore() {
        return previousHighscore;
    }

    public void resetPreviousHighscore() {
        previousHighscore = highscoreService.getPreviousHighscore();
    }
}
