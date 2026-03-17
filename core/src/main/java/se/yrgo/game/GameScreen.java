package se.yrgo.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;


    private final BirbGame game;
    private SpriteBatch batch;
    private Sprite kiwi;
//    private Sprite smallKiwi;

    private Viewport viewport;
    private Camera camera;

    private Sprite background;
    private float bgX1 = 0;
    private float bgX2;
    private float bgSpeed = 100; // pixlar per sekund

    public GameScreen(BirbGame game) {
        this.game = game;

        batch = new SpriteBatch();

        kiwi = new Sprite(new Texture("Kiwi_wing_up.png"));
        kiwi.setSize(153.6f, 102.4f);
        kiwi.setPosition(WORLD_WIDTH / 5, WORLD_HEIGHT / 2);

        // Background
        background = new Sprite(new Texture("background.png"));
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0, 0);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH, WORLD_HEIGHT, 0);
        camera.update();
//
//        background = new Texture("background.png");
//
//        // Placera två kopior av bakgrunden bredvid varandra
//        bgX2 = background.getWidth();
    }

    @Override
    public void render(float delta) {
        draw();

        // Uppdatera bakgrundens position (åt höger)
//        bgX1 += bgSpeed * delta;
//        bgX2 += bgSpeed * delta;
//
//        // Looping åt höger
//        if (bgX1 >= background.getWidth()) {
//            bgX1 = bgX2 - background.getWidth();
//        }
//        if (bgX2 >= background.getWidth()) {
//            bgX2 = bgX1 - background.getWidth();
//        }
//
//        // Rensa skärmen
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rita bakgrunden
//        batch.begin();
//        batch.draw(background, bgX1, 0);
//        batch.draw(background, bgX2, 0);


//        batch.end();

    }

    public void draw() {
        ScreenUtils.clear(Color.GRAY);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        background.draw(batch);
        kiwi.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
//        background.dispose();
    }
}

