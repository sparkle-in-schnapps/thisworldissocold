/*
 * Copyright (C) 2015 yew_mentzaki
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.twisc.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author yew_mentzaki
 */
public class JSoundManager {
    static ArrayList<JLoadTask> load(){
        ArrayList<JLoadTask> jlt = getTasks(new File("res/textures"));
        sounds = new Sound[jlt.size()];
        names = new String[jlt.size()];
        return jlt;
    }
    private static ArrayList<JLoadTask> getTasks(File file){
        File[] files = file.listFiles();
        ArrayList<JLoadTask> jlt = new ArrayList<JLoadTask>();
        for (File f : files) {
            if(f.isDirectory()){
                for (JLoadTask task : getTasks(f)) {
                    jlt.add(task);
                }
            }else{
                final File imageFile = f;
                
                jlt.add(new JLoadTask("Loading texture \"" + imageFile.getAbsolutePath().replace(new File("res/textures").getAbsolutePath()+System.getProperty("file.separator"), "") + "\"...") {

                    @Override
                    void load() {
                        loadImage(imageFile);  
                    }
                });
            }
        }
       
        return jlt;
    }
    
    private static int number = 0;
    private static Sound[] sounds;
    private static String[] names;
    public static Sound getSound(String name){
        for (int i = 0; i < names.length; i++) {
            if(name.equals(names[i]))return sounds[i];
        }
        return null;
    }
    private static void loadImage(File image){
        String shortName = image.getAbsolutePath().replace(new File("res/textures").getAbsolutePath()+System.getProperty("file.separator"), "");
        String format = shortName.substring(shortName.lastIndexOf(".")+1, shortName.length()).toUpperCase();
        try {
            Sound sound = new SoundImpl(new FileInputStream(image), "OGG");
            sounds[number] = sound;
            names[number] = shortName;
        } catch (IOException ex) {
            
        } catch (SlickException ex) {
            Logger.getLogger(JSoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        number++;
    } 

    private static class SoundImpl extends Sound {

        public SoundImpl(InputStream in, String ref) throws SlickException {
            super(in, ref);
        }

        @Override
        public void play(float pitch, float volume) {
            super.play(pitch, (float) (volume * (Twisc.settings.get("sound").getValueAsDouble()/100.0))); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void playAt(float pitch, float volume, float x, float y, float z) {
            super.playAt(pitch, (float) (volume * (Twisc.settings.get("sound").getValueAsDouble()/100.0)), x, y, z); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void play() {
            super.play(1, (float) (1 * (Twisc.settings.get("sound").getValueAsDouble()/100.0)));
        }
        
        @Override
        public void loop() {
            super.loop(1, (float) (1 * (Twisc.settings.get("sound").getValueAsDouble()/100.0))); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void loop(float pitch, float volume) {
            super.loop(pitch, (float) (volume * (Twisc.settings.get("sound").getValueAsDouble()/100.0))); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
