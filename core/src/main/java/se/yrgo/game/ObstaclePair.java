package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import java.util.concurrent.ThreadLocalRandom;

public class ObstaclePair implements Pool.Poolable {
    private Obstacle knife;
    private Obstacle fork;
    private Rectangle positionKnife;
    private Rectangle positionFork;
    private boolean alive;

    public ObstaclePair(Obstacle k, Obstacle f) {
        this.knife = k;
        this.fork = f;

        positionKnife = new Rectangle(knife.getX(), knife.getY(), knife.getWidth(), knife.getHeight());
        positionFork = new Rectangle(fork.getX(), fork.getY(), fork.getWidth(), fork.getHeight());

        alive = false;
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
        return positionFork.getX() < -GameScreen.OBSTACLE_WIDTH;
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

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        fork.setX(screenWidth);
        knife.setX(screenWidth);
    }

    public void init() {
        float y = randomizeYPosition();
        fork.setY(y);
        knife.setY(y + GameScreen.OBSTACLE_GAP + GameScreen.OBSTACLE_HEIGHT);

        alive = true;
    }

    private float randomizeYPosition() {
        float y = ThreadLocalRandom.current().nextInt(
            GameScreen.OBSTACLE_HEIGHT / 5, GameScreen.OBSTACLE_HEIGHT - (GameScreen.OBSTACLE_HEIGHT / 4)) * -1;
        return y;
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
