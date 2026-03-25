package se.yrgo.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ObstaclePair {
    private Obstacle knife;
    private Obstacle fork;
    private Rectangle positionKnife;
    private Rectangle positionFork;


    public ObstaclePair(Obstacle k, Obstacle f) {
        this.knife = k;
        this.fork = f;

        positionKnife = new Rectangle(knife.getX(), knife.getY(), knife.getWidth(), knife.getHeight());
        positionFork = new Rectangle(fork.getX(), fork.getY(), fork.getWidth(), fork.getHeight());
    }

    public void update(float delta) {
        knife.update(delta);
        fork.update(delta);

        positionKnife.setX(knife.getX());
        positionKnife.setY(knife.getY());

        positionFork.setX(fork.getX());
        positionFork.setY(fork.getY());
    }

    public Rectangle getPositionFork() {
        return positionFork;
    }

    public Rectangle getPositionKnife() {
        return positionKnife;
    }

    public float getX() {
       return knife.getX();
    }

    public float getWidth() {
        return knife.getWidth();
    }

    public void setX(float x) {
        fork.setX(x);
        knife.setX(x);
    }

    public void setY(float forkY, float knifeY) {
        fork.setY(forkY);
        knife.setY(knifeY);
    }

    public void render(SpriteBatch batch) {
        knife.render(batch);
        fork.render(batch);
    }
}
