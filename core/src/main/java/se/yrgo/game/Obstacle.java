package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle {

    Texture texture;
    float x, y;
    float speed;

    float width;
    float height;

    public Obstacle(String file, float x, float y, float speed, float width, float height) {
        this.texture = new Texture(file);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
    }

    public void update(float delta) {
        x -= speed * delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return width;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
