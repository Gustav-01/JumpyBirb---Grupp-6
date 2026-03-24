package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * The main screen of the game, where the game mechanics take place.
 */
public class GameScreen implements Screen {
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final int OBSTACLE_GAP = 150;

    private final BirbGame game;
    private SpriteBatch batch;
    private Sprite kiwi;

    private Viewport viewport;
    private Camera camera;

    private Sprite background;

    private Obstacle obstacle1;
    private Obstacle obstacle2;

    private List<ObstaclePair> obstaclePairs = new ArrayList<>();

    private int screenWidth;

    // Variables for jump logic
    private float velocityY = 0;
    private final float gravity = -900f;
    private final float jumpForce = 350f;

    public GameScreen(BirbGame game) {
        this.game = game;

        batch = new SpriteBatch();

        kiwi = new Sprite(new Texture("Kiwi_wing_up.png"));
        kiwi.setSize(153.6f, 102.4f);
        kiwi.setPosition(WORLD_WIDTH / 5f, WORLD_HEIGHT / 2f);

        // Background
        background = new Sprite(new Texture("background.png"));
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0, 0);

        screenWidth = Gdx.graphics.getWidth();

        obstacle1 = new Obstacle("ForkSprite.png", screenWidth + 200, -100, 150);
        obstacle2 = new Obstacle("KnifeSprite.png", screenWidth + 200, (obstacle1.getY() + obstacle1.getHeight() + OBSTACLE_GAP), 150);

        ObstaclePair pair1 = new ObstaclePair(obstacle1, obstacle2);
        obstaclePairs.add(pair1);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH, WORLD_HEIGHT, 0);
        camera.update();
    }

    /**
     * Called once per frame. Updates the game state every frame.
     *
     * @param delta The time in seconds since the last render. By multiplying delta time with movement speed,
     *              we can set a consistant speed (in seconds) regardless of the user's framerate.
     */
    @Override
    public void render(float delta) {
        draw(delta);

        // Jump logic
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velocityY = jumpForce;
        }
        velocityY += gravity * delta;
        float newPosition = kiwi.getY() + velocityY * delta;
        kiwi.setY(newPosition);

        if (kiwi.getY() < 0) {
            kiwi.setY(0);
            velocityY = 0;
        }

    }

    /**
     * Helper method for drawing all textures in a frame.
     */
    public void draw(float delta) {
        ScreenUtils.clear(Color.GRAY);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        background.draw(batch);
        kiwi.draw(batch);

        // Uppdatera hinder
        obstaclePairs.get(0).update(delta);

        // Looping
        if (obstaclePairs.get(0).getX() + obstaclePairs.get(0).getWidth() <= 0) {
            obstaclePairs.get(0).setX(screenWidth + 200);
        }

        // Rita hinder
        obstaclePairs.get(0).render(batch);

        batch.end();
    }

    /**
     * Called when the application window is resized
     *
     * @param width  new width
     * @param height new height
     */
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
        obstacle1.texture.dispose();
        obstacle2.texture.dispose();
    }
}

