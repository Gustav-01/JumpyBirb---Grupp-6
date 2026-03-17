package se.yrgo.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {

    private final BirbGame game;

    private Texture background;
    private float bgX1 = 0;
    private float bgX2;
    private float bgSpeed = 100; // pixlar per sekund

    public GameScreen(BirbGame game) {
        this.game = game;

        background = new Texture("background.png");

        // Placera två kopior av bakgrunden bredvid varandra
        bgX2 = background.getWidth();
    }

    @Override
    public void render(float delta) {

        // Uppdatera bakgrundens position (åt höger)
        bgX1 += bgSpeed * delta;
        bgX2 += bgSpeed * delta;

        // Looping åt höger
        if (bgX1 >= background.getWidth()) {
            bgX1 = bgX2 - background.getWidth();
        }
        if (bgX2 >= background.getWidth()) {
            bgX2 = bgX1 - background.getWidth();
        }

        // Rensa skärmen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rita bakgrunden
        game.batch.begin();
        game.batch.draw(background, bgX1, 0);
        game.batch.draw(background, bgX2, 0);
        game.batch.end();

    }

    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        background.dispose();
    }
}

