package se.yrgo.game.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import se.yrgo.game.constants.GameFunctionalityConstants;
import se.yrgo.game.constants.ObstacleConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a pair of obstacles that move together.
 * Handles position updates, rendering, and pooling state.
 */
public class ObstaclePair implements Pool.Poolable {
    private final Obstacle knife;
    private final Obstacle fork;

    private Rectangle knifeBorderPos;
    private Rectangle forkBorderPos;
    private static final float knifeBorderWidthCutRate = ObstacleConstants.KNIFE_SHAPE_WIDTH / 5f;


    private final List<Rectangle> obstacleCollidableShapes = new ArrayList<>();
    private boolean alive;
    private boolean isScored;

    /**
     * Creates a new obstacle pair using two obstacle objects.
     *
     * @param k the upper obstacle
     * @param f the lower obstacle
     */
    public ObstaclePair(Obstacle k, Obstacle f) {
        this.knife = k;
        this.fork = f;

        initCollisionShapes();

        alive = false;
        isScored = false;
    }

    private void initCollisionShapes(){
        knifeBorderPos = new Rectangle(
            knife.getX() + (knifeBorderWidthCutRate * 2),
            knife.getY(),
            ObstacleConstants.KNIFE_SHAPE_WIDTH - (knifeBorderWidthCutRate * 2),
            ObstacleConstants.OBSTACLE_HEIGHT
        );
        obstacleCollidableShapes.add(knifeBorderPos);

        forkBorderPos = new Rectangle(
            fork.getX(),
            fork.getY(),
            ObstacleConstants.OBSTACLE_WIDTH,
            ObstacleConstants.OBSTACLE_HEIGHT
        );
        obstacleCollidableShapes.add(forkBorderPos);
    }

    public void update(float delta) {
        knife.update(delta);
        fork.update(delta);

        knifeBorderPos.setX(knife.getX() + knifeBorderWidthCutRate * 2);
        knifeBorderPos.setY(knife.getY());
        forkBorderPos.setX(fork.getX());
        forkBorderPos.setY(fork.getY());

        if (outsideScreen()) {
            alive = false;
        }
    }

    private boolean outsideScreen() {
        return fork.getX() < -ObstacleConstants.OBSTACLE_WIDTH;
    }

    public void render(SpriteBatch batch) {
        knife.render(batch);
        fork.render(batch);
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void reset() {
        alive = false;
        isScored = false;


        fork.setX(GameFunctionalityConstants.WORLD_WIDTH);
        knife.setX(GameFunctionalityConstants.WORLD_WIDTH);
    }

    public void init(float speed, int gap) {
        float y = randomizeYPosition();
        fork.setY(y);
        knife.setY(y + gap + ObstacleConstants.OBSTACLE_HEIGHT);

        fork.setSpeed(speed);
        knife.setSpeed(speed);

        alive = true;
    }

    private float randomizeYPosition() {
        float y = ThreadLocalRandom.current().nextInt(
            ObstacleConstants.OBSTACLE_HEIGHT / 5,
            ObstacleConstants.OBSTACLE_HEIGHT - (ObstacleConstants.OBSTACLE_HEIGHT / 4)) * -1f;
        return y;
    }

    /**
     * Check if kiwi have passed the obstaclePair to increase the current score
     *
     * @param kiwiX position of the kiwi
     * @return true if kiwi is past the obstaclePair, false if not
     */
    public boolean checkIfPassed(float kiwiX) {
        float obstacleRightEdge = fork.getX() + fork.getWidth();

        if (!isScored && kiwiX > obstacleRightEdge) {
            isScored = true;
            return true;
        }

        return false;
    }

    public List<Rectangle> getObstacleCollidableShapes() {
        return Collections.unmodifiableList(obstacleCollidableShapes);
    }

    public Rectangle getKnifeBorderPos() {
        return knifeBorderPos;
    }

    public Rectangle getForkBorderPos() {
        return forkBorderPos;
    }

    public void dispose() {
        knife.dispose();
        fork.dispose();
    }
}
