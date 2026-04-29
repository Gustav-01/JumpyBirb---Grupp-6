package se.yrgo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import se.yrgo.game.BirbGame;
import se.yrgo.game.constants.Difficulty;

public class StartScreen implements Screen {

    private final BirbGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public StartScreen(BirbGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("smallFontNew.fnt"));
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        font.draw(batch, "Choose difficulty:", 100, 300);
        font.draw(batch, "1 - Easy",           100, 250);
        font.draw(batch, "2 - Medium",   100, 220);
        font.draw(batch, "3 - Hard",           100, 190);
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

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
