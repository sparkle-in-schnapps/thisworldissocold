package org.twisc.core.gameplay;

import java.awt.Point;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Graphics;
import org.twisc.core.Twisc;

/**
 *
 * @author yew_mentzaki & whizzpered
 */
public class TerrainChunk {
    int x, y;
    byte[][] t = new byte[16][16];
    /*public void render(Graphics g, Point camera){
        glTranslated(x*1536, y*1536, 0);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if(Math.abs(camera.x/96-x)<=Twisc.display_width/2 && Math.abs(camera.y/96-y)<=Twisc.display_height/2){
                    glTranslated(x*96, y*96, 0);
                    Block.block[t[x][y]].render(g);
                    glTranslated(-x*96, -y*96, 0);
                }
            }
        }
        glTranslated(-x*1536, -y*1536, 0);
    }*/
    public Block getBlock(int x, int y){
        if(x>=this.x*1536&&x<this.x*1536+1536&&y>=this.y*1536&&y<this.y*1536+1536){
            x -= this.x*1536;
            y -= this.y*1536;
            x /= 96;
            y /= 96;
            return Block.block[t[x][y]];
        }
        return null;
    }
}
