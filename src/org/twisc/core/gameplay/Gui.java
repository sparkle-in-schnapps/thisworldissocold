package org.twisc.core.gameplay;

import java.awt.Point;
import java.util.Calendar;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.sparkle.fontrender.FontRender;
import org.twisc.core.gameplay.creatures.Player;

/**
 * @author Whizzpered
 */
public class Gui {

    Sprite sprite;
    public Player player;
    Point location = new Point();
    String type;
    int last = 0, fps = 0, frames = 0;

    ;
    
    public void init(Object... args) {
        location.x = (Integer) args[0];
        location.y = (Integer) args[1];
        sprite = new Sprite("gui/" + args[2].toString());
        type = args[3].toString();
        if(args.length>4)
            player = (Player)args[4];
    }

    public void render(Graphics g) {
        sprite.guirender(this);
        if (type.equals("fps2")) {
            frames++;
            if (last != Calendar.getInstance().get(Calendar.SECOND)) {
                fps = frames;
                frames = 0;
                last = Calendar.getInstance().get(Calendar.SECOND);
            }
            FontRender.getTextRender("Sans", 0, 10).drawString("" + fps, 6, 20, Color.black);
        }

    }
}
