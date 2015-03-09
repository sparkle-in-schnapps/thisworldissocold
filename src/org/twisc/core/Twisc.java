package org.twisc.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;
import org.sparkle.fontrender.FontRender;
import org.sparkle.jcfg.JCFG;
import org.sparkle.jcfg.Parser;
import org.twisc.core.gameplay.*;

/**
 *
 * @author yew_mentzaki
 */
public class Twisc {

    public static JCFG settings, conf = new JCFG();
    public static World world;

    public static void main(String[] args) {
        setNatives();
        setGraphics();
    }

    public static void setNatives() {

        if (!new File("native").exists()) {
            JOptionPane.showMessageDialog(null, "Error!\nNative libraries not found!");
            System.exit(1);
        }
        try {

            System.setProperty("java.library.path", new File("native").getAbsolutePath());

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);

            try {
                fieldSysPath.set(null, null);
            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            } catch (IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            }
        } catch (NoSuchFieldException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        }

    }
    public static int display_width;
    public static int display_height;

    public static void setGraphics() {
        Thread renderingThread;
        renderingThread = new Thread("Main Rendering Thread") {

            @Override
            public void run() {
                try {
                    Graphics gr = new Graphics();
                    int w = 800, h = 600, x = 0, y = 0;
                    File cfg = new File("conf.cfg");
                    if (cfg.exists()) {
                        conf = Parser.parse(cfg);
                        w = conf.get("w").getValueAsInteger();
                        h = conf.get("h").getValueAsInteger();
                        x = conf.get("x").getValueAsInteger();
                        y = conf.get("y").getValueAsInteger();
                    } else {
                        conf.set("music", true);
                        conf.set("sound", true);
                    }
                    Display.setDisplayMode(new DisplayMode(w, h));
                    Display.setLocation(x, y);
                    Display.setTitle("This World is so Cold");
                    Display.setResizable(true);

                    try {
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                        System.exit(1);
                    }
                    /*
                     Display.setIcon(new ByteBuffer[]{
                     new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("textures/icons/icon.png")), false, false, null),
                     new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("textures/icons/icon.png")), false, false, null)
                     });
                     */
                    Display.create(new PixelFormat(0, 4, 0, 4));
                    Image logo = new Image("res/textures/gui/logo.png");
                    {
                        display_width = Display.getWidth();
                        display_height = Display.getHeight();
                        GL11.glClearColor(0, 0, 0, 1);
                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glMatrixMode(GL11.GL_PROJECTION);
                        GL11.glLoadIdentity();
                        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glLoadIdentity();
                        logo.draw(display_width / 2 - logo.getWidth() / 2, display_height / 2 - logo.getHeight() / 2);
                        Display.update();
                    }
                    FontRender fontRender = FontRender.getTextRender("Sans", 0, 20);
                    ArrayList<JLoadTask> jlt = new ArrayList<JLoadTask>();
                    jlt.addAll(JMusicManager.load());
                    jlt.addAll(JTextureManager.load());
                    //jlt.addAll(JSoundManager.load());
                    Image loading = new Image("res/textures/gui/loading.png");
                    float jlt_length = jlt.size();
                    while (jlt.size() > 0) {
                        display_width = Display.getWidth();
                        display_height = Display.getHeight();
                        GL11.glClearColor(0, 0, 0, 1);
                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glMatrixMode(GL11.GL_PROJECTION);
                        GL11.glLoadIdentity();
                        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glLoadIdentity();
                        logo.draw(display_width / 2 - logo.getWidth() / 2, display_height / 2 - logo.getHeight() / 2);

                        {
                            float a = (1 - ((float) jlt.size() / jlt_length)) * ((float) display_width);

                            Texture t = loading.getTexture();
                            t.setTextureFilter(GL11.GL_NEAREST);
                            int aa = ((int)a / (int)t.getImageWidth() * (int)t.getImageWidth());
                            
                            for (int i = 0; i < aa/t.getImageWidth(); i++) {
                                loading.draw(t.getImageWidth() * i, display_height - 50, t.getImageWidth(), t.getImageHeight());
                            }
                            
                            if(a-aa>0){
                            t.bind();
                            
                            GL11.glBegin(GL11.GL_QUADS);
                            GL11.glTexCoord2d(0, 0);
                            GL11.glVertex2d(aa, display_height);
                            GL11.glTexCoord2d(((double) t.getWidth()) * ((float)(a-aa) / (double) t.getImageWidth()), 0);
                            GL11.glVertex2d(a, display_height);
                            GL11.glTexCoord2d(((double) t.getWidth()) * ((float)(a-aa) / (double) t.getImageWidth()), t.getHeight());
                            GL11.glVertex2d(a, display_height - 50);
                            GL11.glTexCoord2d(0, t.getHeight());
                            GL11.glVertex2d(aa, display_height - 50);
                            GL11.glEnd();
                            }
                                    
                        }
                        Display.update();
                        jlt.get(0).load();
                        jlt.remove(0);
                    }
                    Block.init();
                    //JMusicManager.start();
                    World world = new World();
                    while (true) {
                        display_width = Display.getWidth();
                        display_height = Display.getHeight();
                        GL11.glClearColor(0, 0, 0, 1);
                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glMatrixMode(GL11.GL_PROJECTION);
                        GL11.glLoadIdentity();
                        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glLoadIdentity();
                        world.render(gr);
                        Display.update();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        renderingThread.start();
    }
}
