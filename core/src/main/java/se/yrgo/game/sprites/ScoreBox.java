package se.yrgo.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import se.yrgo.game.constants.GameFunctionalityConstants;

public class ScoreBox {
    private BitmapFont font;
    private Sprite scoreBox;

    public ScoreBox() {
        font = new BitmapFont(Gdx.files.internal("smallFontNew.fnt"));
        font.setColor(new Color(0.878f, 0.655f, 0.220f, 0.8f));
        scoreBox = new Sprite(new Texture("scorebox.png"));
        scoreBox.setSize(GameFunctionalityConstants.SCORE_BOX_WIDTH, GameFunctionalityConstants.SCORE_BOX_HEIGHT);
        scoreBox.setPosition(0,
            GameFunctionalityConstants.WORLD_HEIGHT - scoreBox.getHeight());
        scoreBox.setAlpha(0.8f);
    }

    public void draw(SpriteBatch batch, int currentScore, int highscore) {
        scoreBox.draw(batch);
        font.draw(batch,
            String.format("%d", currentScore),
            (scoreBox.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH / 4f)),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
        font.draw(batch,
            String.format("%d", highscore),
            (scoreBox.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH * 0.75f) - 10),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
    }

    public void dispose() {
        scoreBox.getTexture().dispose();
        font.dispose();
    }
}
