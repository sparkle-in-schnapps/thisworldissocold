package org.twisc.core.gameplay.creatures;

import java.io.Serializable;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.input.Keyboard.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.twisc.core.JTextureManager;
import org.twisc.core.gameplay.*;

/**
 *
 * @author yew_mentzaki
 */
public class Mannequin extends Entity implements Serializable{

    @Override
    public void render(Graphics g) {
        Image player = new Image(JTextureManager.getTexture("creatures/player/side.png"));
        player.draw(-player.getWidth()*3/2, -player.getHeight()*3, player.getWidth()*3, player.getHeight()*3);
        
    }
    
}
