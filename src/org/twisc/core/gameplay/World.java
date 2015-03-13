package org.twisc.core.gameplay;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.sparkle.jbind.*;
import org.twisc.core.Twisc;
import org.twisc.core.gameplay.creatures.Player;
import sun.java2d.pipe.RenderBuffer;

/**
 *
 * @author yew_mentzaki & whizzpered
 */
public final class World {

    public final Point targetCamera = new Point(0, 0);
    private Point camera = new Point(0, 0);
    public static Random random = new Random();

    public World() {
        for (int i = 0; i < 5; i++) {
            timers[i] = new Timer(25, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tick();
                }
            });
        }
        init();
        checkChunk();
        checkChunkThread.setPriority(Thread.MIN_PRIORITY);
        checkChunkThread.setDaemon(true);
        checkChunkThread.start();
    }
    /*
     Чунга-чанга, лето круглый год
     На Таймыре всё наоборот
     */
    private Thread checkChunkThread = new Thread("Check chunk thread") {
        @Override
        public void run() {
            while (true) {
                try {
                    checkChunk();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void checkChunk() {
        Point camera = this.camera;
        ArrayList<TerrainChunk> checked = new ArrayList<TerrainChunk>();
        for (int x = -2; x <= 1; x++) {
            for (int y = -2; y <= 1; y++) {
                boolean unloaded = true;
                for (TerrainChunk chunk : terrain) {
                    if ((chunk.x == (camera.x / 1536) + x) && (chunk.y == (camera.y / 1536) + y)) {
                        unloaded = false;
                        checked.add(chunk);
                        break;
                    }
                }
                if (unloaded) {
                    Point coord = new Point(((camera.x / 1536) + x), ((camera.y / 1536) + y));
                    File file = new File("terrain/part_" + String.valueOf(coord.x).replace('-', 'n') + "_" + String.valueOf(coord.y).replace('-', 'n') + ".chunk");
                    TerrainChunk chunk = new TerrainChunk();
                    chunk.x = coord.x;
                    chunk.y = coord.y;
                    if (file.exists()) {
                        try {
                            JBinD bind = Reader.read(file);

                            byte[] ter = bind.getPart("terrain").getDataAsByteArray();
                            int caret = 0;
                            for (int i = 0; i < 16; i++) {
                                for (int j = 0; j < 16; j++) {
                                    chunk.t[i][j] = ter[caret++];
                                }
                            }
                            for (Part part : bind.getAllParts()) {
                                if (part.getTitle().equals("terrain")) {
                                    continue;
                                }
                                byte[] bytes = part.getDataAsByteArray();
                                Entity entity = (Entity) SerializationUtils.deserialize(bytes);
                                entity.world = this;
                                entities.add(entity);
                            }
                            System.out.println("terrain/part_" + String.valueOf(coord.x).replace('-', 'n') + "_" + String.valueOf(coord.y).replace('-', 'n') + ".chunk is loaded.");
                        } catch (IOException ex) {
                            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                if (random.nextInt(4) == 0) {
                                    chunk.t[i][j] = (byte) random.nextInt(3);
                                }
                            }
                        }

                        System.out.println("terrain/part_" + String.valueOf(coord.x).replace('-', 'n') + "_" + String.valueOf(coord.y).replace('-', 'n') + ".chunk is created.");
                    }
                    terrain.add(chunk);
                    checked.add(chunk);
                }
            }
        }
        for (int i = 0; true; i++) {
            if (i >= terrain.size()) {
                break;
            }
            if (!checked.contains(terrain.get(i))) {
                TerrainChunk chunk = terrain.get(i);
                terrain.remove(i);
                /**
                 * **********
                 */
                try {
                    JBinD bind = new JBinD();

                    File file = new File("terrain/part_" + String.valueOf(chunk.x).replace('-', 'n') + "_" + String.valueOf(chunk.y).replace('-', 'n') + ".chunk");

                    byte[] ter = new byte[256];
                    int caret = 0;
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            ter[caret++] = chunk.t[x][y];
                        }
                    }
                    try {
                        bind.addPart(new Part("terrain", ter));
                        Entity[] entities = entitiesFromChunk(chunk.x + 1536, chunk.y * 1536);
                        int num = 0;
                        for (Entity e : entities) {
                            bind.addPart(new Part("e" + (num++), SerializationUtils.serialize(e)));
                        }
                    } catch (JBinDException ex) {
                        Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Writer.write(bind, file);
                    System.out.println("terrain/part_" + String.valueOf(chunk.x).replace('-', 'n') + "_" + String.valueOf(chunk.y).replace('-', 'n') + ".chunk is saved.");
                } catch (IOException ex) {
                    Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                }
                /**
                 * **********
                 */
                i--;
            }
        }
    }

    public void init() {
        spawn("creatures.Player", 0.0, 0.0);
        guis.add(new Gui());
        guis.add(new Gui());
        guis.get(1).init(0, 20, "button_left", "fps1");
        guis.get(2).init(10, 20, "button_right", "fps2");
        pause();
    }

    public void pause() {
        Thread t = new Thread("pause/unpause thread") {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (timers[i].isRunning()) {
                        timers[i].stop();
                    } else {
                        timers[i].start();
                    }
                }
            }
        };
        t.start();
    }

    public void spawn(String entity, Object... args) {
        try {
            Class clazz = Class.forName("org.twisc.core.gameplay." + entity);
            Entity e = (Entity) clazz.newInstance();
            e.world = this;
            entities.add(e);
            e.init(args);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tick() {
        for (Entity e : entitiesForTick()) {
            e.tick();
        }
        for (int i = 0; true; i++) {
            if (i >= entities.size()) {
                break;
            }
            if (entities.get(i).flag_RemoveEntityFromWorld) {
                entities.remove(i);
                i--;
            }
        }
    }

    public void render(Graphics g) {
        camera.x = targetCamera.x;
        camera.y = targetCamera.y;
        Keyboard.updateKeyboard();

        TerrainChunk terrain[] = new TerrainChunk[this.terrain.size()];
        for (int i = 0; true; i++) {
            if (i >= terrain.length || i >= this.terrain.size()) {
                break;
            }
            terrain[i] = this.terrain.get(i);
        }
        TerrainChunk currChunk = terrain[0];
        for (int x = camera.x / 96 - 1 -  Display.getWidth() / 2 / 96; x <= camera.x / 96 + Display.getWidth() / 2 / 96 + 1; x += 1) {
            for (int y = camera.y / 96 - 1 -  Display.getWidth() / 2 / 96; y <= camera.y / 96 + Display.getHeight() / 2 / 96 + 1; y += 1) {
                Block b = currChunk.getBlock(x * 96, y * 96);
                if (b == null) {
                    for (TerrainChunk cc : terrain) {
                        b = cc.getBlock(x * 96, y * 96);
                        if (b != null) {
                            currChunk = cc;
                            break;
                        }
                    }
                }
                if (b != null) {
                    glTranslated(x*96 - camera.x + Twisc.display_width / 2, y*96 - camera.y + Twisc.display_height / 2, 0);
                    b.render(g);
                    glLoadIdentity();
                }
            }
        }
        for (Entity e : entitiesForRender()) {
            double x = e.x, y = e.y;
            glTranslated(x - camera.x + Twisc.display_width / 2, y - camera.y + Twisc.display_height / 2, 0);
            e.render(g);
            glLoadIdentity();
        }
        for (Gui gui : guis) {
            gui.render(g);
        }
    }
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<TerrainChunk> terrain = new ArrayList<TerrainChunk>();

    public Entity[] entitiesForTick() {
        ArrayList<Entity> e = new ArrayList<Entity>();
        for (int i = 0; i < entities.size(); i++) {
            Entity en = entities.get(i);
            if (abs(en.x - camera.x) < Twisc.display_width && abs(en.y - camera.y) < Twisc.display_height) {
                e.add(en);
            }
        }
        Entity[] entities = new Entity[e.size()];
        for (int i = 0; i < e.size(); i++) {
            entities[i] = e.get(i);
        }
        return entities;
    }

    public Entity[] entitiesFromChunk(int x, int y) {
        ArrayList<Entity> e = new ArrayList<Entity>();
        for (int i = 0; i < entities.size(); i++) {
            Entity en = entities.get(i);
            if (en instanceof Player) {
                continue;
            }
            if (abs(en.x - x + 768) <= 768 && abs(en.y - y + 768) <= 768) {
                e.add(en);
                entities.remove(i);
                i--;
            }
        }
        Entity[] entities = new Entity[e.size()];
        for (int i = 0; i < e.size(); i++) {
            entities[i] = e.get(i);
        }
        return entities;
    }

    public Entity[] entitiesForRender() {
        ArrayList<Entity> e = new ArrayList<Entity>();
        for (int i = 0; i < entities.size(); i++) {
            Entity en = entities.get(i);
            if (abs(en.x - camera.x) < Twisc.display_width / 2 && abs(en.y - camera.y) < Twisc.display_height / 2) {
                e.add(en);

            }
        }
        for (int i = 0; i < e.size(); i++) {
            int h = i;
            for (int j = 0; j < e.size(); j++) {
                if (e.get(h) == e.get(j)) {
                    continue;
                }
                if (e.get(h).y > e.get(j).y) {
                    Collections.swap(e, h, j);
                    h = j;

                }
            }
        }
        Entity[] entities = new Entity[e.size()];
        for (int i = 0; i < e.size(); i++) {
            entities[i] = e.get(i);
        }
        return entities;
    }
    private Timer[] timers = new Timer[5];
    public ArrayList<Gui> guis = new ArrayList<Gui>();
}
