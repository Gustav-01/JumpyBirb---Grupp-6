package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class KiwiSprite {
    private Texture texture;
    private Circle bodyPosition;
    private Sprite sprite;

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

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

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
