package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import se.yrgo.game.constants.GameFunctionalityConstants;
import se.yrgo.game.constants.KiwiConstants;

/**
 * Class for the kiwi sprite containing logics for updating and tracking position, drawing sprite and checking for
 * overlaps against other {@code Shape2D} objects.
 */
public class KiwiSprite {
    private Texture texture;
    private Circle bodyPosition;
    private Sprite sprite;
    private float velocityY = 0f;

    /**
     * Creates a new kiwi sprite from the given texture and dimensions.
     *
     * @param texture    texture to build the kiwi's sprite on
     * @param kiwiWidth  the total width from wing to wing
     * @param kiwiHeight the total height
     * @param initXPos   where on X to initially position the kiwi
     * @param initYPos   where on Y to initially position the kiwi
     */
    public KiwiSprite(Texture texture, float kiwiWidth, float kiwiHeight, float initXPos, float initYPos) {
        this.texture = texture;
        this.sprite = new Sprite(texture);

        this.sprite.setSize(kiwiWidth, kiwiHeight);
        this.sprite.setPosition(initXPos, initYPos);

        bodyPosition = new Circle(sprite.getX() + sprite.getWidth() / 2f,
            sprite.getY() + sprite.getHeight() / 2f, sprite.getHeight() / 2f);
    }

    public void update(float delta) {
        // jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velocityY = KiwiConstants.JUMPFORCE;
        }
        // gravity
        velocityY += GameFunctionalityConstants.GRAVITY * delta;

        float newY = sprite.getY() + velocityY * delta;

        float minY = 0;
        float maxY = GameFunctionalityConstants.WORLD_HEIGHT - KiwiConstants.KIWI_HEIGHT / 2f;

        if (newY < minY) {
            newY = minY;
            velocityY = 0;
        } else if (newY > maxY) {
            newY = maxY;
            velocityY = 0;
        }

        sprite.setY(newY);

        bodyPosition.setY(sprite.getY() + sprite.getHeight() / 2f);
        bodyPosition.setX(sprite.getX() + sprite.getWidth() / 2f);

    }

    /**
     * Draw the kiwi to the given {@link SpriteBatch}.
     *
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Checks to see if the kiwi overlaps with a {@link Rectangle} shape. Use for collision detection.
     *
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

    public void dispose() {
        texture.dispose();
    }
}
