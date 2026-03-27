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
import se.yrgo.game.constants.GameFunctionalityConstants;
import se.yrgo.game.constants.KiwiConstants;
import se.yrgo.game.constants.ObstacleConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * The main screen of the game, where the game mechanics take place.
 */
public class GameScreen implements Screen {

    private final BirbGame game;
    private SpriteBatch batch;
    private Sprite kiwi;
    private Sprite background;

    private Viewport viewport;
    private Camera camera;

    private Pool<ObstaclePair> obstaclePool;
    private List<ObstaclePair> activeObstacles = new ArrayList<>();
    private float secondsPassed;

    // Variables for jump logic
    private float velocityY = 0;
    private final float gravity = -900f;
    private final float jumpForce = 350f;

    public GameScreen(BirbGame game) {
        this.game = game;
        batch = new SpriteBatch();

        kiwi = new Sprite(new Texture("Kiwi_wing_up.png"));
        kiwi.setSize(KiwiConstants.KIWI_WIDTH, KiwiConstants.KIWI_HEIGHT);

        // Background
        background = new Sprite(new Texture("background.png"));
        background.setSize(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, camera);


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

        if (secondsPassed > GameFunctionalityConstants.SECONDS_BETWEEN_OBSTACLES) {
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
        secondsPassed = 0;
        activeObstacles.clear();
        obstaclePool.clear();

        kiwi.setPosition(GameFunctionalityConstants.WORLD_WIDTH / 5f, GameFunctionalityConstants.WORLD_HEIGHT / 2f);
        background.setPosition(0, 0);
        camera.position.set(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, 0);
        camera.update();

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
                    GameFunctionalityConstants.WORLD_WIDTH,
                    0,
                    ObstacleConstants.OBSTACLE_SPEED,
                    ObstacleConstants.OBSTACLE_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT
                );
                var knife = new Obstacle(
                    "KnifeSprite.png",
                    GameFunctionalityConstants.WORLD_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT + ObstacleConstants.OBSTACLE_GAP,
                    ObstacleConstants.OBSTACLE_SPEED,
                    ObstacleConstants.OBSTACLE_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT
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

    }
}

