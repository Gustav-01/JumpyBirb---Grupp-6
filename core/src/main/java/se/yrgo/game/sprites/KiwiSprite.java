package se.yrgo.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private Animation<TextureRegion> flapAnimation;
    private TextureRegion idleFrame;

    private boolean isFlapping;
    private float stateTime = 0f;
    private Circle bodyPosition;
    private float velocityY = 0f;

    private int circleYPixelTweak = 26;
    private int circleRadiusPixelTweak = 16;

    private float x;
    private float y;
    private float width;
    private float height;

    private Texture wingUp;
    private Texture wingDown;

    /**
     * Creates a new kiwi sprite from the given texture and dimensions.
     *
     * @param wingUp     the image of kiwi with wings up
     * @param wingDown   the image of kiwi with wings down
     * @param kiwiWidth  the total width from wing to wing
     * @param kiwiHeight the total height
     * @param initXPos   where on X to initially position the kiwi
     * @param initYPos   where on Y to initially position the kiwi
     */
    public KiwiSprite(Texture wingUp, Texture wingDown, float kiwiWidth, float kiwiHeight, float initXPos, float initYPos) {
        this.wingUp = wingUp;
        this.wingDown = wingDown;

        this.width = kiwiWidth;
        this.height = kiwiHeight;
        this.x = initXPos;
        this.y = initYPos;

        idleFrame = new TextureRegion(wingUp);

        TextureRegion[] frames = new TextureRegion[]{
            new TextureRegion(wingUp),
            new TextureRegion(wingDown),
            new TextureRegion(wingUp)
        };

        flapAnimation = new Animation<>(0.1f, frames);
        flapAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        bodyPosition = new Circle(
            initXPos + kiwiWidth / 2f,
            initYPos + kiwiHeight / 2f - circleYPixelTweak,
            (kiwiHeight - circleRadiusPixelTweak) / 2f);
    }

    /**
     * Call every render cycle to update the kiwi's position, including logic for user input to jump.
     *
     * @param delta
     */

    public void update(float delta) {
        stateTime += delta;
        // jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            velocityY = KiwiConstants.JUMPFORCE;

            isFlapping = true;
            stateTime = 0f;
        }

        if (isFlapping && flapAnimation.isAnimationFinished(stateTime)) {
            isFlapping = false;
        }
        // gravity
        velocityY += GameFunctionalityConstants.GRAVITY * delta;

        float newY = y + velocityY * delta;

        float minY = 0;
        float maxY = GameFunctionalityConstants.WORLD_HEIGHT - height / 2f;

        if (newY < minY) {
            newY = minY;
            velocityY = 0;
        } else if (newY > maxY) {
            newY = maxY;
            velocityY = 0;
        }

        y = newY;

        bodyPosition.setY(y + height / 2f);
        bodyPosition.setX(x + width / 2f);

    }

    /**
     * Draw the kiwi to the given {@link SpriteBatch}.
     *
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        TextureRegion frame;

        if (isFlapping) {
            frame = flapAnimation.getKeyFrame(stateTime);
        } else {
            frame = idleFrame;
        }

        batch.draw(frame, x, y, width, height);
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
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get the {@Link Circle} with the same radius and position as the kiwi sprite. Suitable for collision checking.
     *
     * @return
     */
    public Circle getBodyPosition() {
        return bodyPosition;
    }

    public void dispose() {
        wingUp.dispose();
        wingDown.dispose();
    }
}
