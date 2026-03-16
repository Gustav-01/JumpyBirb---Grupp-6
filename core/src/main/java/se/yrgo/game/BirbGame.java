package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * The main game class, containing application life-cycle methods.
 */
public class BirbGame extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;
    private Sprite kiwi;
    private Viewport viewport;
    private Camera camera;

    /**
     * Runs one time at the start of the application.
     */
    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        assetManager.load("birb_the_kiwi.png", Texture.class);
        assetManager.finishLoading();
        kiwi = new Sprite(assetManager.get("birb_the_kiwi.png", Texture.class));
        kiwi.setSize(1.5f, 1f);

        camera = new PerspectiveCamera();
        viewport = new FitViewport(800, 400, camera);
    }

    /**
     * Is called when rendering should be performed.
     */
    @Override
    public void render() {


        draw();
    }


    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(kiwi, 0, 0, 1.5f, 1);
        batch.end();
    }

//    Todo: Make sure resizing is done correctly, possibly using this method
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height, true);
//    }
}
