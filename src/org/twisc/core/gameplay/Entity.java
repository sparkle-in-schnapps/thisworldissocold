package org.twisc.core.gameplay;

import java.io.Serializable;
import org.newdawn.slick.*;

/**
 *
 * @author yew_mentzaki & whizzpered
 */
public class Entity implements Serializable{
    public double x, y;
    transient public World world;
    transient boolean flag_RemoveEntityFromWorld = false;
    public Entity() {
        
    }
    /*
    args[0] - X
    args[1] - Y
    */
    public void init(Object... args){
        x = (Double)args[0];
        y = (Double)args[1];
    }
    public void tick(){
        
    }
    public void remove(){
        flag_RemoveEntityFromWorld = true;
    }
    public void render(Graphics g){
        g.setColor(Color.yellow);
        g.fillOval(-10, -10, 20, 20);
    }
}
