package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import se.yrgo.game.constants.GameFunctionalityConstants;

public class GameOverScreen implements Screen {
    private BirbGame game;

    private BitmapFont bigFont;
    private BitmapFont smallFont;
    private SpriteBatch batch = new SpriteBatch();
    private int currentScore;

    public GameOverScreen(BirbGame game, int currentScore) {
        this.game = game;

        this.bigFont = new BitmapFont();
        final Color fontColor = Color.WHITE;
        this.bigFont.setColor(fontColor);

        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.bigFont.getData().setScale(2.5f);

        this.smallFont = new BitmapFont();
        this.smallFont.setColor(fontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(1.5f);

        this.currentScore = currentScore;

    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);

        batch.begin();

        float worldWidth = GameFunctionalityConstants.WORLD_WIDTH;
        float worldHeight = GameFunctionalityConstants.WORLD_HEIGHT;

        float centerX = 0;
        float width = worldWidth;

        float centerY = worldHeight / 2f;

        // Game Over text
        bigFont.draw(batch, "Game Over!",
            centerX,
            centerY + 100,
            width,
            Align.center,
            false
        );

        // Score
        smallFont.draw(batch, "Score: " + currentScore,
            centerX,
            centerY + 30,
            width,
            Align.center,
            false
        );

        // Restart instruction
        smallFont.draw(batch, "Press SPACE to restart",
            centerX,
            centerY - 40,
            width,
            Align.center,
            false
        );


        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        bigFont.dispose();
        smallFont.dispose();
    }
}
