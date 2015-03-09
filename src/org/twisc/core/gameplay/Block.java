package org.twisc.core.gameplay;

import org.newdawn.slick.Image;
import org.twisc.core.JTextureManager;
import org.newdawn.slick.Graphics;

/**
 *
 * @author yew_mentzaki
 */
public class Block {
    /*Static Block contains all block types*/
    public static Block[] block = new Block[Byte.MAX_VALUE];
    public static void init(){
        block[0] = new Block("Grass", "tiles/grass.png", true);
                
    }
    /*Block type*/

    public Block(String name, String image, boolean passable) {
        this.name = name;
        this.image = new Image(JTextureManager.getTexture(image));
        this.passable = passable;
    }
    public String name; 
    public Image image;
    public boolean passable;

    void render(Graphics g) {
        image.draw(0, 0, 96, 96);
    }
}
