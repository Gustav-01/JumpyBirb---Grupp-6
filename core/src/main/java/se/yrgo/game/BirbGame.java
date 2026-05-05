package se.yrgo.game;

import com.badlogic.gdx.Game;
import se.yrgo.game.constants.Difficulty;
import se.yrgo.game.screens.GameOverScreen;
import se.yrgo.game.screens.GameScreen;
import se.yrgo.game.screens.StartScreen;
import se.yrgo.game.services.HighscoreService;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {
    private HighscoreService highscoreService;
    private Difficulty difficulty = Difficulty.MEDIUM;

    private int previousHighscore;

    /**
     * Runs one time at the start of the application.
     * Initializes the highscore service and shows the start screen.
     */
    @Override
    public void create() {
        highscoreService = new HighscoreService();
        previousHighscore = highscoreService.getPreviousHighscore(difficulty);

        setScreen(new StartScreen(this));
    }

    /**
     * Saves the final score for the current difficulty.
     *
     * @param score the score achieved by the player
     */
    public void saveScore(int score) {
        highscoreService.registerFinalScore(score, difficulty);
    }

    /**
     * Returns the stored highscore for the current difficulty.
     *
     * @return the previous highscore value
     */
    public int getPreviousHighscore() {
        return previousHighscore;
    }

    /**
     * Reloads the highscore from the highscore service.
     * Used after saving a new score.
     */
    public void resetPreviousHighscore() {
        previousHighscore = highscoreService.getPreviousHighscore(difficulty);
    }

    /**
     * @return the currently selected difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the active difficulty level.
     *
     * @param difficulty the new difficulty to use
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Disposes of game resources and the highscore service.
     */
    @Override
    public void dispose() {
        super.dispose();
        highscoreService.dispose();
    }
}
