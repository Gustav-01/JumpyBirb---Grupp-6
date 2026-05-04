package se.yrgo.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import se.yrgo.game.constants.GameFunctionalityConstants;

/**
 * Displays the current score and the highscore.
 * Draws a background box and renders the numbers inside it.
 */
public class ScoreBox {
    private BitmapFont font;
    private Sprite scoreBoxSprite;

    /**
     * Creates a new score box with a background image and font.
     * Positions the box at the top-left corner of the screen.
     */
    public ScoreBox() {
        font = new BitmapFont(Gdx.files.internal("smallFontNew.fnt"));
        font.setColor(new Color(0.878f, 0.655f, 0.220f, 0.8f));
        scoreBoxSprite = new Sprite(new Texture("scorebox.png"));
        scoreBoxSprite.setSize(GameFunctionalityConstants.SCORE_BOX_WIDTH, GameFunctionalityConstants.SCORE_BOX_HEIGHT);
        scoreBoxSprite.setPosition(0,
            GameFunctionalityConstants.WORLD_HEIGHT - scoreBoxSprite.getHeight());
        scoreBoxSprite.setAlpha(0.8f);
    }

    /**
     * Draws the score box and the current score values.
     *
     * @param batch        the SpriteBatch used for rendering
     * @param currentScore the player's current score
     * @param highscore    the stored highscore
     */
    public void draw(SpriteBatch batch, int currentScore, int highscore) {
        scoreBoxSprite.draw(batch);
        font.draw(batch,
            String.format("%d", currentScore),
            (scoreBoxSprite.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH / 4f)),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
        font.draw(batch,
            String.format("%d", highscore),
            (scoreBoxSprite.getX() + (GameFunctionalityConstants.SCORE_BOX_WIDTH * 0.75f) - 10),
            (GameFunctionalityConstants.WORLD_HEIGHT - (GameFunctionalityConstants.SCORE_BOX_HEIGHT / 2f)),
            GameFunctionalityConstants.WORLD_WIDTH,
            Align.left,
            false);
    }

    /**
     * Disposes the texture and font resources used by the score box.
     */
    public void dispose() {
        scoreBoxSprite.getTexture().dispose();
        font.dispose();
    }
}
