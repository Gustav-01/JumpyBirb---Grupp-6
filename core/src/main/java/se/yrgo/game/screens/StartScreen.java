package se.yrgo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import se.yrgo.game.BirbGame;
import se.yrgo.game.constants.Difficulty;

public class StartScreen implements Screen {

    private final BirbGame game;
    private SpriteBatch batch;
    private Texture menuTexture;
    private FitViewport viewport;
    private OrthographicCamera camera;

    public StartScreen(BirbGame game) {
        this.game = game;
        batch = new SpriteBatch();
        menuTexture = new Texture(Gdx.files.internal("startMenuScreen.png"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(menuTexture, 0, 0, 800, 480);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            startGame(Difficulty.EASY);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            startGame(Difficulty.MEDIUM);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            startGame(Difficulty.HARD);
        }
    }

    private void startGame(Difficulty difficulty) {
        game.setDifficulty(difficulty);
        game.resetPreviousHighscore();
        game.setScreen(new GameScreen(game));
        dispose();
    }


    @SuppressWarnings("java:S1186")
    @Override public void show() {}
    @SuppressWarnings("java:S1186")
    @Override public void hide() {}
    @SuppressWarnings("java:S1186")
    @Override public void pause() {}
    @SuppressWarnings("java:S1186")
    @Override public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        menuTexture.dispose();
    }
}
