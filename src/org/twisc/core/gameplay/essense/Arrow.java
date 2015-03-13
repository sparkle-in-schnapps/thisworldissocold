
package org.twisc.core.gameplay.essense;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.twisc.core.gameplay.Entity;
import org.twisc.core.gameplay.creatures.Player;

/**
 *
 * @author Whizzpered
 */
public class Arrow extends Entity{
    public double x, y,ex, ey, angle;
    static final double v=3;
    public int dmg;
    Image texture;
    
    
    public Arrow(String type, Player player) {
        this.x = player.x;
        this.y=player.y;
        //ex = player.aim.x;
        //ey = player.aim.y;
        angle = Math.atan2(y-ey,x-ex);
        try {
            texture = new Image("/res/weapons/arrow/"+type +".png");
        } catch (SlickException ex) {
            Logger.getLogger(Arrow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void tick(){
        y+=v*Math.sin(angle);
        x+=v*Math.cos(angle);
    }
    
    public void render(Graphics g) {
        texture.setCenterOfRotation((float)x, (float)y);
        texture.rotate((float)angle);
        g.drawImage(texture,(int)x,(int)y);
    }
}
