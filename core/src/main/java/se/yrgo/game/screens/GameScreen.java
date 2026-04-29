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

import java.util.ArrayList;
import java.util.List;


/**
 * The main screen of the game, where the game mechanics take place.
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
    private BitmapFont font;
    private Sprite scoreBox;

    private float currentObstacleSpeed;

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

        //Score box
        font = new BitmapFont(Gdx.files.internal("smallFontNew.fnt"));
        font.setColor(new Color(0.878f, 0.655f, 0.220f, 0.7f));
        scoreBox = new Sprite(new Texture("scorebox.png"));
        scoreBox.setSize(GameFunctionalityConstants.SCORE_BOX_WIDTH, GameFunctionalityConstants.SCORE_BOX_HEIGHT);
        scoreBox.setPosition(0,
            GameFunctionalityConstants.WORLD_HEIGHT - scoreBox.getHeight());
        scoreBox.setAlpha(0.7f);

        //Background Music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("BirbEasy.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f); //1.f = full volume

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
        draw(delta);

        checkForGameOver();

    }

    /**
     * Provides the logic of the jump movement of the kiwi
     *
     * @param delta
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

        scoreBox.draw(batch);
        font.draw(batch,
            String.format("%d", currentScore),
            (scoreBox.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH / 4f)),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
        font.draw(batch,
            String.format("%d", game.getPreviousHighscore()),
            (scoreBox.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH * 0.75f) - 10),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
//        GlyphLayout layout = new GlyphLayout(font, String.format("""
//            %d               %d""", currentScore, game.getPreviousHighscore()));

//        font.draw(batch, layout, 0,
//            GameFunctionalityConstants.WORLD_HEIGHT);

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

    private void spawnObstacle() {
        var obstacle = obstaclePool.obtain();
        obstacle.reset();
        obstacle.init(currentObstacleSpeed);
        activeObstacles.add(obstacle);
    }

    private void initPool() {
        this.obstaclePool = new Pool<ObstaclePair>() {
            @Override
            protected ObstaclePair newObject() {

                var fork = new Obstacle("ForkSprite.png",
                    GameFunctionalityConstants.WORLD_WIDTH,
                    0,
                    ObstacleConstants.OBSTACLE_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT
                );
                var knife = new Obstacle(
                    "KnifeSprite.png",
                    GameFunctionalityConstants.WORLD_WIDTH,
                    ObstacleConstants.OBSTACLE_HEIGHT + ObstacleConstants.OBSTACLE_GAP,
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
        hasPaused = true;
    }

    @Override
    public void dispose() {
        batch.dispose();
        kiwi.dispose();
        background.getTexture().dispose();
        font.dispose();
        backgroundMusic.dispose();
        scoreBox.getTexture().dispose();

        // Dispose all active obstacles
        for (ObstaclePair obs : activeObstacles) {
            obs.dispose();
        }

    }
}

