package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a single obstacle in the game.
 * Handles position, movement, size, and rendering.
 */
public class Obstacle {

    Texture texture;
    float x, y;
    float speed;

    float width;
    float height;

    /**
     * Creates a new obstacle with a texture, position, speed, and size.
     *
     * @param file   path to the texture file
     * @param x      starting x-position
     * @param y      starting y-position
     * @param width  obstacle width
     * @param height obstacle height
     */
    public Obstacle(String file, float x, float y, float width, float height) {
        this.texture = new Texture(file);
        this.x = x;
        this.y = y;
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

    public void dispose() {
        texture.dispose();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

}
