package org.twisc.core.gameplay.creatures;

import java.io.Serializable;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.input.Keyboard.*;
import org.newdawn.slick.Graphics;
import org.twisc.core.gameplay.*;
import static org.twisc.core.gameplay.Side.*;

/**
 *
 * @author yew_mentzaki
 */
public class Player extends Entity implements Serializable{
    static Sprite sprite = new Sprite("creatures/player/");
    Side side;
    @Override
    public void render(Graphics g) {
        if(Keyboard.isKeyDown(KEY_RIGHT)||Keyboard.isKeyDown(KEY_D)){
            x += 10;
            side = RIGHT;
        }
        if(Keyboard.isKeyDown(KEY_LEFT)||Keyboard.isKeyDown(KEY_A)){
            x -= 10;
            side = LEFT;
        }
        if(Keyboard.isKeyDown(KEY_DOWN)||Keyboard.isKeyDown(KEY_S)){
            y += 10;
            side = FRONT;
        }
        if(Keyboard.isKeyDown(KEY_UP)||Keyboard.isKeyDown(KEY_W)){
            y -= 10;
            side = BACK;
        }
        world.targetCamera.x = (int) x;
        world.targetCamera.y = (int) y;
        sprite.render(side);
    }
    
}
