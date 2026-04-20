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


    @Override
    public void show() {
        background.setPosition(0, 0);
        camera.position.set(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        ScreenUtils.clear(Color.GRAY);
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

                Press SPACE to restart""", currentScore, game.getPreviousHighscore()),
            centerX,
            centerY + 100,
            worldWidth,
            Align.center,
            false
        );

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //Wait one second before restarting, in case player just pressed space.
            if (elapsedTime > 1) {
                game.setScreen(new GameScreen(game));
                dispose();
                game.resetPreviousHighscore();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        customFont.dispose();
        smallCustomFont.dispose();
        background.getTexture().dispose();
    }
}
