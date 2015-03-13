package org.twisc.core.gameplay;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.twisc.core.JTextureManager;
import static org.twisc.core.gameplay.Side.*;

/**
 *
 * @author yew_mentzaki & whizzpered
 */
public class Sprite {

    String s;

    public Sprite(String s) {
        this.s = s;
    }

    Image back = null, front = null, side = null;

    public void render(Side spriteSide) {
        if (back == null) {
            back = new Image(JTextureManager.getTexture(s + "back.png"));

            front = new Image(JTextureManager.getTexture(s + "front.png"));

            side = new Image(JTextureManager.getTexture(s + "side.png"));
        }
        if (spriteSide == FRONT) {
            front.draw(-front.getWidth() * 3 / 2, -front.getHeight() * 3, front.getWidth() * 3, front.getHeight() * 3);
        }
        if (spriteSide == BACK) {
            back.draw(-back.getWidth() * 3 / 2, -back.getHeight() * 3, back.getWidth() * 3, back.getHeight() * 3);
        }
        if (spriteSide == RIGHT) {
            side.draw(-side.getWidth() * 3 / 2, -side.getHeight() * 3, side.getWidth() * 3, side.getHeight() * 3);
        }
        if (spriteSide == LEFT) {
            GL11.glScaled(-1, 1, 1);
            side.draw(-side.getWidth() * 3 / 2, -side.getHeight() * 3, side.getWidth() * 3, side.getHeight() * 3);
            GL11.glScaled(-1, 1, 1);
        }

    }
    Image guis, guis2;

    public void guirender(Gui gui) {
        if (gui.type.equals("scale")) {
            if (guis == null) {
                guis = new Image(JTextureManager.getTexture(s + ".png"));
            }
            if (guis2 == null) {
                guis2 = new Image(JTextureManager.getTexture(s + "_sc.png"));
            }
            guis.draw(gui.location.x, gui.location.y);
            guis2.getTexture().bind();
            GL11.glTranslated(gui.location.x, gui.location.y, 0);
            GL11.glBegin(GL11.GL_QUADS);

            float hp = (((float) gui.player.currhp) / (float) gui.player.thishp);
            {
                GL11.glTexCoord2f(0f, 0f);
                GL11.glVertex2d(0f, 0f);
                GL11.glTexCoord2f(hp * guis2.getTextureWidth(), 0f);
                GL11.glVertex2d(hp * guis2.getWidth(), 0f);
                GL11.glTexCoord2f(hp * guis2.getTextureWidth(), 1f * guis2.getTextureHeight());
                GL11.glVertex2d(hp * guis2.getWidth(), guis2.getHeight());
                GL11.glTexCoord2f(0f, 1f * guis2.getTextureHeight());
                GL11.glVertex2d(0f, guis2.getHeight());
            }
            GL11.glEnd();
            GL11.glTranslated(-gui.location.x, -gui.location.y, 0);
        } else {
            if (guis == null) {
                guis = new Image(JTextureManager.getTexture(s + ".png"));
            }
            guis.draw(gui.location.x, gui.location.y);
        }
    }
}
