package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle {

    Texture texture;
    float x, y;
    float speed;

    float width = 52;
    float height = 320;

    public Obstacle(String file, float x, float y, float speed) {
        this.texture = new Texture(file);
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update(float delta) {
        x -= speed * delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
