package org.twisc.core.gameplay;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Graphics;

/**
 *
 * @author yew_mentzaki
 */
public class TerrainChunk {
    int x, y;
    byte[][] t = new byte[16][16];
    public void render(Graphics g){
        glTranslated(x*1536, y*1536, 0);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                glTranslated(x*96, y*96, 0);
                Block.block[t[x][y]].render(g);
                glTranslated(-x*96, -y*96, 0);
            }
        }
        glTranslated(-x*1536, -y*1536, 0);
    }
}
