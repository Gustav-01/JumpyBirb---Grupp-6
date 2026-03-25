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
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The main screen of the game, where the game mechanics take place.
 */
public class GameScreen implements Screen {
    //TODO cleanup: Move constants to seperate classes

    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    public static final int OBSTACLE_GAP = 150;
    public static final int OBSTACLE_SPEED = 150;
    public static final int OBSTACLE_INTERVAL = Gdx.graphics.getWidth() / 3;
    public static final int OBSTACLE_WIDTH = 52;
    public static final int OBSTACLE_HEIGHT = 360;

    private float secondsPassed;

    private final BirbGame game;
    private SpriteBatch batch;
    private Sprite kiwi;

    private Viewport viewport;
    private Camera camera;

    private Sprite background;

    private Obstacle obstacle1;
    private Obstacle obstacle2;

    private Pool<ObstaclePair> obstaclePool;
    private List<ObstaclePair> activeObstacles = new ArrayList<>();

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
        updateState(delta);

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

    private void updateState(float delta) {
        secondsPassed += delta;

        if (secondsPassed > 3f) {
            spawnObstacle();
            secondsPassed = 0;
        }

        List<ObstaclePair> toRemove = new ArrayList<>();
        for (ObstaclePair obs : activeObstacles) {
            obs.update(delta);

            if (!obs.isAlive()) {
                toRemove.add(obs);
                obstaclePool.free(obs);
            }
        }

        activeObstacles.removeAll(toRemove);
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


        for (ObstaclePair obs : activeObstacles) {
            obs.render(batch);
        }

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
        initPool();

        spawnObstacle();
    }

    private void spawnObstacle() {
        var obstacle = obstaclePool.obtain();
        obstacle.init();
        activeObstacles.add(obstacle);
    }

    private void initPool() {
        this.obstaclePool = new Pool<ObstaclePair>() {
            @Override
            protected ObstaclePair newObject() {

                var fork = new Obstacle("ForkSprite.png",
                    WORLD_WIDTH,
                    0,
                    OBSTACLE_SPEED,
                    OBSTACLE_WIDTH,
                    OBSTACLE_HEIGHT
                );
                var knife = new Obstacle(
                    "KnifeSprite.png",
                    WORLD_WIDTH,
                    OBSTACLE_HEIGHT + OBSTACLE_GAP,
                    OBSTACLE_SPEED,
                    OBSTACLE_WIDTH,
                    OBSTACLE_HEIGHT
                );

                return new ObstaclePair(
                    knife,
                    fork
                );
            }
        };
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

