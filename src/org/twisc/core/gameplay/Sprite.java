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
        if(back == null){
            back = new Image(JTextureManager.getTexture(s + "back.png"));
            
            front = new Image(JTextureManager.getTexture(s + "front.png"));
            
            side = new Image(JTextureManager.getTexture(s + "side.png"));
        }
        if (spriteSide == FRONT) {
            front.draw(-front.getWidth()*3/2, -front.getHeight()*3, front.getWidth()*3, front.getHeight()*3);
        }
        if (spriteSide == BACK) {
            back.draw(-back.getWidth()*3/2, -back.getHeight()*3, back.getWidth()*3, back.getHeight()*3);
        }
        if (spriteSide == RIGHT) {
            side.draw(-side.getWidth()*3/2, -side.getHeight()*3, side.getWidth()*3, side.getHeight()*3);
        }
        if (spriteSide == LEFT) {
            GL11.glScaled(-1, 1, 1);
            side.draw(-side.getWidth()*3/2, -side.getHeight()*3, side.getWidth()*3, side.getHeight()*3);
            GL11.glScaled(-1, 1, 1);
        }
        
    }
    Image guis;
    public void guirender(Gui gui) {
        if(guis == null)guis = new Image(JTextureManager.getTexture(s));
        guis.draw(gui.location.x, gui.location.y);
    }
}
