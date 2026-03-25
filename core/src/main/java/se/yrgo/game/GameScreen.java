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
import java.util.concurrent.ThreadLocalRandom;

/**
 * The main screen of the game, where the game mechanics take place.
 */
public class GameScreen implements Screen {
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final int OBSTACLE_GAP = 150;
    private static final int OBSTACLE_SPEED = 150;
    private static final int OBSTACLE_INTERVAL = Gdx.graphics.getWidth() / 3;
    private static final int OBSTACLE_WIDTH = 52;
    private static final int OBSTACLE_HEIGHT = 360;

    private final BirbGame game;
    private SpriteBatch batch;
    private Sprite kiwi;

    private Viewport viewport;
    private Camera camera;

    private Sprite background;

    private Obstacle obstacle1;
    private Obstacle obstacle2;

    private List<ObstaclePair> obstaclePairs;

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
//
//        obstacle1 = new Obstacle("ForkSprite.png", screenWidth + 200, -100, 150);
//        obstacle2 = new Obstacle("KnifeSprite.png", screenWidth + 200, (obstacle1.getY() + obstacle1.getHeight() + OBSTACLE_GAP), 150);
//
//        ObstaclePair pair1 = new ObstaclePair(obstacle1, obstacle2);
//        obstaclePairs.add(pair1);
//
//        ObstaclePair pair2 = new ObstaclePair(
//            new Obstacle("ForkSprite.png", screenWidth + 500, 0, 150),
//            new Obstacle("KnifeSprite.png", screenWidth + 500, (obstacle1.getY() + obstacle1.getHeight() + OBSTACLE_GAP), 150));
//
//        obstaclePairs.add(pair2);
//
//        ObstaclePair pair3 = new ObstaclePair(
//            new Obstacle("ForkSprite.png", screenWidth + 800, -300, 150),
//            new Obstacle("KnifeSprite.png", screenWidth + 800, (obstacle1.getY() + obstacle1.getHeight() + OBSTACLE_GAP), 150));
//
//        obstaclePairs.add(pair3);

        obstaclePairs = getObstacles();

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
        for (ObstaclePair obstaclePair : obstaclePairs) {
            obstaclePair.update(delta);

            // Looping
            if (obstaclePair.getX() + obstaclePair.getWidth() <= 0) {
                if (obstaclePairs.indexOf(obstaclePair) == 0) {
                    obstaclePair.setX(
                        obstaclePairs.getLast()
                            .getPositionFork().getX() + obstaclePair.getWidth() + OBSTACLE_INTERVAL
                    );

                    float newYPos = randomizeYPosition();
                    obstaclePair.setY(newYPos, newYPos + OBSTACLE_HEIGHT + OBSTACLE_GAP);
                } else {
                    obstaclePair.setX(
                        obstaclePairs.get(obstaclePairs.indexOf(obstaclePair) - 1)
                            .getPositionFork().getX() + obstaclePair.getWidth() + OBSTACLE_INTERVAL
                    );
                    float newYPos = randomizeYPosition();
                    obstaclePair.setY(newYPos, newYPos + OBSTACLE_HEIGHT + OBSTACLE_GAP);
                }

            }

            // Rita hinder
            obstaclePair.render(batch);
        }


        batch.end();
    }

    private List<ObstaclePair> getObstacles() {
        List<ObstaclePair> obstacles = new ArrayList<>();

        int obstacleTotalCount = 5;
        for (int i = 0; i < obstacleTotalCount; i++) {
            obstacles.add(generateObstacle(obstacleTotalCount, i));
        }

        return obstacles;
    }

    private ObstaclePair generateObstacle(int obstacleTotalCount, int index) {
//        obstacle1 = new Obstacle("ForkSprite.png", screenWidth + OBSTACLE_INTERVAL, -100, 150);
//        obstacle2 = new Obstacle("KnifeSprite.png", screenWidth + OBSTACLE_INTERVAL,
//            (obstacle1.getY() + obstacle1.getHeight() + OBSTACLE_GAP), 150);

//        float x = (WORLD_WIDTH / (float)obstacleTotalCount + WORLD_WIDTH + (index * (WORLD_WIDTH / (float)obstacleTotalCount)));
        float x = (WORLD_WIDTH + OBSTACLE_INTERVAL + (index * (OBSTACLE_INTERVAL + OBSTACLE_WIDTH)));
        float y = randomizeYPosition();

        var obstacle1 = new Obstacle("ForkSprite.png", x, y, OBSTACLE_SPEED, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        var obstacle2 = new Obstacle(
            "KnifeSprite.png", x, y + obstacle1.getHeight() + OBSTACLE_GAP, OBSTACLE_SPEED, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

        return new ObstaclePair(
            obstacle1,
            obstacle2
        );

    }

    private float randomizeYPosition() {
        float y = ThreadLocalRandom.current().nextInt(OBSTACLE_HEIGHT - (OBSTACLE_HEIGHT / 5)) * -1;
        return y;
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

