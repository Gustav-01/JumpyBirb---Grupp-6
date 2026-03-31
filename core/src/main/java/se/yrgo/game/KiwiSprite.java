package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class for the kiwi sprite containing logics for updating and tracking position, drawing sprite and checking for
 * overlaps against other {@code Shape2D} objects.
 */
public class KiwiSprite {
    private Texture texture;
    private Circle bodyPosition;
    private Sprite sprite;

    /**
     * Creates a new kiwi sprite from the given texture and dimensions.
     * @param texture texture to build the kiwi's sprite on
     * @param kiwiWidth the total width from wing to wing
     * @param kiwiHeight the total height
     * @param initXPos where on X to initially position the kiwi
     * @param initYPos where on Y to initially position the kiwi
     */
    public KiwiSprite(Texture texture, float kiwiWidth, float kiwiHeight, float initXPos, float initYPos) {
        this.texture = texture;
        this.sprite = new Sprite(texture);

        this.sprite.setSize(kiwiWidth, kiwiHeight);
        this.sprite.setPosition(initXPos, initYPos);

        bodyPosition = new Circle(sprite.getX() + sprite.getWidth() / 2f,
            sprite.getY() + sprite.getHeight() / 2f, sprite.getHeight() / 2f);
    }

    public void update() {
        bodyPosition.setY(sprite.getY() + sprite.getHeight() / 2f);
        bodyPosition.setX(sprite.getX() + sprite.getWidth() / 2f);
    }

    /**
     * Draw the kiwi to the given {@link SpriteBatch}.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Checks to see if the kiwi overlaps with a {@link Rectangle} shape. Use for collision detection.
     * @param rectangle the shape to check if it overlaps with the kiwi
     * @return true if it overlaps, otherwise false.
     */
    public boolean overlaps(Rectangle rectangle) {
        return Intersector.overlaps(bodyPosition, rectangle);
    }

    public float getX() {
        return this.sprite.getX();
    }

    public float getY() {
        return this.sprite.getY();
    }

    public void setX(float x) {
        this.sprite.setX(x);
    }

    public void setY(float y) {
        this.sprite.setY(y);
    }
}
