package se.yrgo.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObstaclePair {
    private Obstacle knife;
    private Obstacle fork;

    public ObstaclePair(Obstacle k, Obstacle f) {
        this.knife = k;
        this.fork = f;
    }

    public void update(float delta) {
        knife.update(delta);
        fork.update(delta);
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

    public void render(SpriteBatch batch) {
        knife.render(batch);
        fork.render(batch);
    }
}
