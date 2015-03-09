package org.twisc.core.gameplay.creatures;

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
public class Player extends Entity{

    @Override
    public void render(Graphics g) {
        if(Keyboard.isKeyDown(KEY_RIGHT)||Keyboard.isKeyDown(KEY_D)){
            x += 10;
        }
        if(Keyboard.isKeyDown(KEY_LEFT)||Keyboard.isKeyDown(KEY_A)){
            x -= 10;
        }
        if(Keyboard.isKeyDown(KEY_DOWN)||Keyboard.isKeyDown(KEY_S)){
            y += 10;
        }
        if(Keyboard.isKeyDown(KEY_UP)||Keyboard.isKeyDown(KEY_W)){
            y -= 10;
        }
        world.targetCamera.x = (int) x;
        world.targetCamera.y = (int) y;
        Image player = new Image(JTextureManager.getTexture("creatures/player/right.png"));
        player.draw(-player.getWidth()*3/2, -player.getHeight()*3, player.getWidth()*3, player.getHeight()*3);
    }
    
}
