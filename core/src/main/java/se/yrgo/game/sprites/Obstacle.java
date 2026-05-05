package se.yrgo.game.sprites;

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

    /**
     * Updates the obstacle's position based on its speed.
     *
     * @param delta time since last frame
     */
    public void update(float delta) {
        x -= speed * delta;
    }

    /**
     * Draws the obstacle to the screen.
     *
     * @param batch the SpriteBatch used for rendering
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    /**
     * @return the obstacle's Y position
     */
    public float getY() {
        return y;
    }

    /**
     * @return the obstacle's height
     */
    public float getHeight() {
        return height;
    }

    /**
     * @return the obstacle's X position
     */
    public float getX() {
        return x;
    }

    /**
     * @return the obstacle's width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the obstacle's X position.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the obstacle's Y position.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Set the speed of the obstacle
     * @param speed current speed of the obstacle
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Disposes the texture resource used in this class
     */
    public void dispose() {
        texture.dispose();
    }


}
