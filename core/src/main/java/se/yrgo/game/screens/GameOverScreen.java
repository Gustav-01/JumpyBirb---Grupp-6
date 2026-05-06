package se.yrgo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import se.yrgo.game.BirbGame;
import se.yrgo.game.constants.GameFunctionalityConstants;

/**
 * A LibGDX screen shown when the player collides with an obstacle
 * and loses the game. It displays the final score and the personal best.
 * The player can restart the game or return to the start screen.
 */
public class GameOverScreen implements Screen {
    private BirbGame game;

    private SpriteBatch batch = new SpriteBatch();
    private int currentScore;

    private BitmapFont customFont;
    private BitmapFont smallCustomFont;
    private Sprite background;

    private Viewport viewport;
    private Camera camera;

    private float elapsedTime = 0;

    /**
     * Creates a new Game Over screen with the given score.
     *
     * @param game the instance of the game
     * @param currentScore the score from the finished round
     */
    public GameOverScreen(BirbGame game, int currentScore) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, camera);

        this.background = new Sprite(new Texture("gameover_bg_small.png"));
        background.setSize(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT);

        //Try new font
        this.customFont = new BitmapFont(Gdx.files.internal("customFont.fnt"));
        this.customFont.setColor(new Color(0.494f, 0.788f, 0.0039f, 1.0f));

        this.smallCustomFont = new BitmapFont(Gdx.files.internal("smallFontNew.fnt"));
        this.smallCustomFont.setColor(new Color(0.494f, 0.788f, 0.0039f, 1.0f));

        this.currentScore = currentScore;

    }

    /**
     * Prepares the screen when it becomes visible.
     * Positions the background and updates the camera.
     */
    @Override
    public void show() {
        background.setPosition(0, 0);
        camera.position.set(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, 0);
        camera.update();
    }

    /**
     * Renders the background, score information, and restart instructions.
     * Also listens for input to restart the game or change difficulty.
     *
     * @param delta time since last frame
     */
    @Override
    public void render(float delta) {
        elapsedTime += delta;

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        background.draw(batch);

        float worldHeight = GameFunctionalityConstants.WORLD_HEIGHT;
        float worldWidth = GameFunctionalityConstants.WORLD_WIDTH;

        float centerX = 0;

        float centerY = worldHeight / 2f;

        customFont.draw(batch, "Game Over!",
            centerX,
            centerY + 200,
            worldWidth,
            Align.center,
            false
        );

        smallCustomFont.draw(batch, String.format("""
                Score: %d
                Personal best: %d
                Difficulty: %s

                Press SPACE to restart
                Press 1 to change difficulty""", currentScore, game.getPreviousHighscore(), game.getDifficulty().prettyName),
            centerX,
            centerY + 100,
            worldWidth,
            Align.center,
            false
        );

        batch.end();

        if (elapsedTime > 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.setScreen(new GameScreen(game));
                dispose();
                game.resetPreviousHighscore();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                game.setScreen(new StartScreen(game));
                dispose();
            }
        }

    }

    /**
     * Updates the viewport when the window size changes.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @SuppressWarnings("java:S1186")
    @Override
    public void pause() {
    }

    @SuppressWarnings("java:S1186")
    @Override
    public void resume() {
    }

    @SuppressWarnings("java:S1186")
    @Override
    public void hide() {
    }

    /**
     * DIsposes all graphical resources used by this screen.
     */
    @Override
    public void dispose() {
        batch.dispose();
        customFont.dispose();
        smallCustomFont.dispose();
        background.getTexture().dispose();
    }
}
