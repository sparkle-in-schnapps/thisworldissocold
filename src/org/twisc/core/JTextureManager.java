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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author yew_mentzaki
 */
public class JTextureManager {
    static ArrayList<JLoadTask> load(){
        ArrayList<JLoadTask> jlt = getTasks(new File("res/textures"));
        textures = new Texture[jlt.size()];
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
    private static Texture[] textures;
    private static String[] names;
    public static Texture getTexture(String name){
        
        for (int i = 0; i < names.length; i++) {
            if(name.equals(names[i]))return textures[i];
        }
        return null;
    }
    private static void loadImage(File image){
        String shortName = image.getAbsolutePath().replace(new File("res/textures").getAbsolutePath()+System.getProperty("file.separator"), "");
        String format = shortName.substring(shortName.lastIndexOf(".")+1, shortName.length()).toUpperCase();
        try {
            textures[number] = TextureLoader.getTexture(format, new FileInputStream(image));
            textures[number].setTextureFilter(GL11.GL_NEAREST);
            names[number] = shortName;
        } catch (IOException ex) {
            
        }
        number++;
    } 
}
