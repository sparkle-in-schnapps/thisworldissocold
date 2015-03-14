package org.twisc.core.gameplay;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Whizzpered
 */
public class Menu {
    public ArrayList <Gui> containers = new ArrayList<Gui>();
    
    public void init(Object... args) {
        
    }
    
    public void tick() {
        
    }
    
    public void render(Graphics g) {
        for(Gui gui:containers){
            gui.render(g);
        }
    }
}
