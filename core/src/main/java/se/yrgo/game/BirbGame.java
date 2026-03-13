package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BirbGame extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;
    private Texture kiwi;


    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        assetManager.load("birb_the_kiwi.png", Texture.class);
        assetManager.finishLoading();
        kiwi = assetManager.get("birb_the_kiwi.png", Texture.class);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        batch.draw(kiwi, 50, 50);
        batch.end();
    }
}
