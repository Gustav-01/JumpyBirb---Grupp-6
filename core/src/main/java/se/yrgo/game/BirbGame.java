package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {
    private static final int WORLD_WIDTH = 16;
    private static final int WORLD_HEIGHT = 12;


    private AssetManager assetManager;
    private SpriteBatch batch;
    private Sprite kiwi;
    private Viewport viewport;
    private Camera camera;

    private Sprite smallKiwi;

    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        assetManager.load("birb_the_kiwi.png", Texture.class);

        assetManager.load("Test kiwi no wings-1.png", Texture.class);

        assetManager.finishLoading();

        smallKiwi = new Sprite(assetManager.get("Test kiwi no wings-1.png", Texture.class));
        smallKiwi.setSize(1, 1); //Todo: recalculate
        smallKiwi.setPosition(1,1);

        kiwi = new Sprite(assetManager.get("birb_the_kiwi.png", Texture.class));
        kiwi.setSize(4.8f, 3.2f);
        kiwi.setPosition(8, 8);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH, WORLD_HEIGHT, 0);
        camera.update();

    }

    /**
     * Is called when rendering should be performed.
     */
    @Override
    public void render() {
        super.render();

        draw();
    }


    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
//
//        kiwi.setPosition(5, 5);
//        smallKiwi.setPosition(1, 1);
        batch.begin();
//        batch.draw(kiwi, 50, 50, 1.5f, 1);
        kiwi.draw(batch);
        smallKiwi.draw(batch);
//        batch.draw(kiwi, 10, 5);
//        batch.draw(smallKiwi, 1, 1, 1, 1);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
