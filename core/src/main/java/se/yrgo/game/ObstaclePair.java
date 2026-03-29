package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import se.yrgo.game.constants.ObstacleConstants;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a pair of obstacles that move together.
 * Handles position updates, rendering, and pooling state.
 */
public class ObstaclePair implements Pool.Poolable {
    private Obstacle knife;
    private Obstacle fork;
    private Rectangle positionKnife;
    private Rectangle positionFork;
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

        positionKnife = new Rectangle(knife.getX(), knife.getY(), knife.getWidth(), knife.getHeight());
        positionFork = new Rectangle(fork.getX(), fork.getY(), fork.getWidth(), fork.getHeight());

        alive = false;
        isScored = false;
    }

    public void update(float delta) {
        knife.update(delta);
        fork.update(delta);

        positionKnife.setX(knife.getX());
        positionKnife.setY(knife.getY());

        positionFork.setX(fork.getX());
        positionFork.setY(fork.getY());

        if (outsideScreen()) {
            alive = false;
        }
    }

    private boolean outsideScreen() {
        return positionFork.getX() < -ObstacleConstants.OBSTACLE_WIDTH;
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
        int screenHeight = Gdx.graphics.getHeight();

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


//    public Rectangle getPositionFork() {
//        return positionFork;
//    }
//
//    public Rectangle getPositionKnife() {
//        return positionKnife;
//    }
//
//    public float getX() {
//        return knife.getX();
//    }
//
//    public float getWidth() {
//        return knife.getWidth();
//    }
//
//    public void setX(float x) {
//        fork.setX(x);
//        knife.setX(x);
//    }
//
//    public void setY(float forkY, float knifeY) {
//        fork.setY(forkY);
//        positionFork.setY(forkY);
//
//        knife.setY(knifeY);
//        positionKnife.setY(knifeY);
//    }
}
