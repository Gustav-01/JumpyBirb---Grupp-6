package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
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
    private Rectangle positionKnifeLeft;
    private Rectangle positionKnifeRight;
    private Rectangle positionForkTop;
    private Rectangle positionForkBottom;

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

    private void initCollisionShapes() {
        positionKnifeLeft = new Rectangle(
            knife.getX() + 5,
            knife.getY() + (ObstacleConstants.OBSTACLE_HEIGHT / 7f),
            ObstacleConstants.KNIFE_SHAPE_WIDTH / 2f,
            ObstacleConstants.OBSTACLE_HEIGHT / 3f);
        obstacleCollidableShapes.add(positionKnifeLeft);

        positionKnifeRight = new Rectangle(
            knife.getX() + (ObstacleConstants.KNIFE_SHAPE_WIDTH / 2f),
            knife.getY(),
            ObstacleConstants.KNIFE_SHAPE_WIDTH / 2f,
            knife.getHeight());
        obstacleCollidableShapes.add(positionKnifeRight);

        positionForkTop = new Rectangle(fork.getX(),
            fork.getY() + (ObstacleConstants.OBSTACLE_HEIGHT * 0.65f),
            fork.getWidth(),
            fork.getHeight() - (ObstacleConstants.OBSTACLE_HEIGHT * 0.65f));
        obstacleCollidableShapes.add(positionForkTop);

        positionForkBottom = new Rectangle(fork.getX() + (ObstacleConstants.OBSTACLE_WIDTH / 3f),
            fork.getY(),
            ObstacleConstants.OBSTACLE_WIDTH / 4f,
            ObstacleConstants.OBSTACLE_HEIGHT - positionForkTop.getHeight());
        obstacleCollidableShapes.add(positionForkBottom);

    }

    public void update(float delta) {
        knife.update(delta);
        fork.update(delta);

        positionKnifeLeft.setX(knife.getX() + 5);
        positionKnifeRight.setX(knife.getX() + (ObstacleConstants.KNIFE_SHAPE_WIDTH / 2f));
        positionKnifeLeft.setY(knife.getY() + (ObstacleConstants.OBSTACLE_HEIGHT / 7f));
        positionKnifeRight.setY(knife.getY());

        positionForkTop.setX(fork.getX());
        positionForkTop.setY(fork.getY() + (ObstacleConstants.OBSTACLE_HEIGHT * 0.65f));
        positionForkBottom.setX(fork.getX() + (ObstacleConstants.OBSTACLE_WIDTH / 3f));
        positionForkBottom.setY(fork.getY());

        if (outsideScreen()) {
            alive = false;
        }
    }

    private boolean outsideScreen() {
        return positionForkTop.getX() < -ObstacleConstants.OBSTACLE_WIDTH;
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

        int screenWidth = Gdx.graphics.getWidth();

        fork.setX(screenWidth);
        knife.setX(screenWidth);
    }

    public void init() {
        float y = randomizeYPosition();
        fork.setY(y);
        knife.setY(y + ObstacleConstants.OBSTACLE_GAP + ObstacleConstants.OBSTACLE_HEIGHT);

        alive = true;
    }

    private float randomizeYPosition() {
        float y = ThreadLocalRandom.current().nextInt(
            ObstacleConstants.OBSTACLE_HEIGHT / 5, ObstacleConstants.OBSTACLE_HEIGHT - (ObstacleConstants.OBSTACLE_HEIGHT / 4)) * -1;
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

    public Rectangle getPositionForkTop() {
        return positionForkTop;
    }

    public Rectangle getPositionForkBottom() {
        return positionForkBottom;
    }

    public Rectangle getPositionKnifeLeft() {
        return positionKnifeLeft;
    }

    public Rectangle getPositionKnifeRight() {
        return positionKnifeRight;
    }

    public List<Rectangle> getObstacleCollidableShapes() {
        return Collections.unmodifiableList(obstacleCollidableShapes);
    }

}
