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
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private HighscoreService highscoreService;
    private Difficulty difficulty = Difficulty.MEDIUM;

    private int previousHighscore;

    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        highscoreService = new HighscoreService();
        previousHighscore = highscoreService.getPreviousHighscore(difficulty);

        setScreen(new StartScreen(this));
    }

    public void saveScore(int score) {
        highscoreService.registerFinalScore(score, difficulty);
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
        previousHighscore = highscoreService.getPreviousHighscore(difficulty);
    }

    public Difficulty getDifficulty() { return difficulty; }

    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
 }
