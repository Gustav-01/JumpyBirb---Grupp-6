package se.yrgo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.audio.Music;
import se.yrgo.game.BirbGame;
import se.yrgo.game.sprites.KiwiSprite;
import se.yrgo.game.sprites.Obstacle;
import se.yrgo.game.sprites.ObstaclePair;
import se.yrgo.game.constants.GameFunctionalityConstants;
import se.yrgo.game.constants.KiwiConstants;
import se.yrgo.game.constants.ObstacleConstants;
import se.yrgo.game.sprites.ScoreBox;

import java.util.ArrayList;
import java.util.List;


/**
 * The main screen of the game, where the game mechanics take place.
 * This screen updates the kiwi, spawn obstacles, checks for collisions,
 * and draws all visual elements each frame
 */
public class GameScreen implements Screen {
    private final boolean inDebugMode = false; //For debug purposes
    ShapeRenderer shapeRenderer = new ShapeRenderer(); //For debug purposes

    private final BirbGame game;
    private SpriteBatch batch;
    private KiwiSprite kiwi;
    private Sprite background;

    private Music backgroundMusic;

    private Viewport viewport;
    private Camera camera;

    private Pool<ObstaclePair> obstaclePool;
    private List<ObstaclePair> activeObstacles = new ArrayList<>();

    private float secondsPassed;
    private boolean hasPaused = false;

    private int currentScore;
    private ScoreBox scoreBox;

    private float currentObstacleSpeed;

    /**
     * Creates a new gameplay screen and initializes game objects.
     * @param game instance of the game
     */
    public GameScreen(BirbGame game) {
        this.game = game;
        batch = new SpriteBatch();

        kiwi = new KiwiSprite(
            new Texture("Kiwi_wing_up.png"),
            new Texture("Kiwi_Wing_down.png"),
            KiwiConstants.KIWI_WIDTH,
            KiwiConstants.KIWI_HEIGHT,
            GameFunctionalityConstants.WORLD_WIDTH / 5f,
            GameFunctionalityConstants.WORLD_HEIGHT / 2f
        );

        currentScore = 0;
        this.scoreBox = new ScoreBox();

        //Background Music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("birbmusic-cut.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1.0f); //1.f = full volume

        // Background
        background = new Sprite(new Texture("background.png"));
        background.setSize(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, camera);

        currentObstacleSpeed = ObstacleConstants.OBSTACLE_SPEED;
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
        draw();
        checkForGameOver();
    }

    /**
     * Provides the logic of the jump movement of the kiwi
     *
     * @param delta time since last frame
     */
    private void updateState(float delta) {
        kiwi.update(delta);

        if (hasPaused) {
            hasPaused = false;
            return;
        }
        secondsPassed += delta;
        float spawnInterval = ObstacleConstants.OBSTACLE_DISTANCE / currentObstacleSpeed;
        if (secondsPassed > spawnInterval) {
            spawnObstacle();
            secondsPassed = 0;
        }

        currentObstacleSpeed += 5f * delta;

        List<ObstaclePair> toRemove = new ArrayList<>();
        for (ObstaclePair obs : activeObstacles) {
            obs.update(delta);

            // ----- SCORE LOGIC -----
            if (obs.isAlive() && obs.checkIfPassed(kiwi.getX())) {
                currentScore++;
            }

            if (!obs.isAlive()) {
                toRemove.add(obs);
                obstaclePool.free(obs);
                obs.reset();
            }
        }
        activeObstacles.removeAll(toRemove);
    }

    /**
     * Helper method for drawing all textures in a frame.
     */
    public void draw() {
        ScreenUtils.clear(Color.GRAY);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        background.draw(batch);
        kiwi.draw(batch);

        for (ObstaclePair obs : activeObstacles) {
            obs.render(batch);
        }

        scoreBox.draw(batch, currentScore, game.getPreviousHighscore());

        batch.end();

        //For debugging. May be deleted later
//        drawShapesOutlinesDebug();
    }

    /**
     * For drawing out the shapes making up the sprites, to check positions. Use for debugging collision control.
     */
    private void drawShapesOutlinesDebug() {
        if (inDebugMode) {
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(kiwi.getBodyPosition().x, kiwi.getBodyPosition().y, kiwi.getBodyPosition().radius);

            for (ObstaclePair obs : activeObstacles) {
                shapeRenderer.rect(obs.getKnifeBorderPos().getX(), obs.getKnifeBorderPos().getY(),
                    obs.getKnifeBorderPos().getWidth(), obs.getKnifeBorderPos().getHeight());
                shapeRenderer.rect(obs.getForkBorderPos().getX(), obs.getForkBorderPos().getY(),
                    obs.getForkBorderPos().getWidth(), obs.getForkBorderPos().getHeight());
            }
            shapeRenderer.end();
        }
    }

    /**
     * Checks whether the kiwi has collided with any active obstacle.
     * If a collision is detected, the current GameScreen is disposed
     * and a new GameOverScreen is created.
     */
    private void checkForGameOver() {
        for (ObstaclePair obstacle : activeObstacles) {
            for (Rectangle shape : obstacle.getObstacleCollidableShapes()) {
                if (kiwi.overlaps(shape)) {
                    game.setScreen(new GameOverScreen(game, currentScore));
                    dispose();
                    game.saveScore(currentScore);
                    backgroundMusic.stop();
                    return;
                }
            }
        }
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

    /**
     * Called when the screen becomes visible.
     * Resets state, initializes the obstacle pool,
     * and starts background music.
     */
    @Override
    public void show() {
        secondsPassed = 0;
        activeObstacles.clear();

        background.setPosition(0, 0);
        camera.position.set(GameFunctionalityConstants.WORLD_WIDTH, GameFunctionalityConstants.WORLD_HEIGHT, 0);
        camera.update();

        initPool();

        spawnObstacle();

        backgroundMusic.play();
    }

    /**
     * Spawns a new obstacle pair from the pool.
     */
    private void spawnObstacle() {
        var obstacle = obstaclePool.obtain();
        obstacle.reset();
        obstacle.init(currentObstacleSpeed, game.getDifficulty().obstacleGap);
        activeObstacles.add(obstacle);
    }

    /**
     * Initializes the obstacle pool used for reusing obstacle objects.
     */
    private void initPool() {
        this.obstaclePool = new Pool<ObstaclePair>() {
            @Override
            protected ObstaclePair newObject() {

                int gap = game.getDifficulty().obstacleGap;

                var fork = new Obstacle("ForkSprite.png",
                    GameFunctionalityConstants.WORLD_WIDTH,
                    0,
                    ObstacleConstants.OBSTACLE_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT
                );
                var knife = new Obstacle(
                    "KnifeSprite.png",
                    GameFunctionalityConstants.WORLD_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT + gap, //NOSONAR

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

    @SuppressWarnings("java:S1186")
    @Override
    public void hide() {
    }

    @SuppressWarnings("java:S1186")
    @Override
    public void pause() {
    }

    /**
     * Called when the game resumes from a paused state.
     */
    @Override
    public void resume() {
        hasPaused = true;
    }

    /**
     * Disposes all the graphic resources used on this screen.
     */
    @Override
    public void dispose() {
        batch.dispose();
        kiwi.dispose();
        background.getTexture().dispose();
        scoreBox.dispose();
        backgroundMusic.dispose();

        // Dispose all active obstacles
        for (ObstaclePair obs : activeObstacles) {
            obs.dispose();
        }

    }
}

