/*
 * Copyright (C) 2015 yew_mentzaki & whizzpered
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
import java.util.Random;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

/**
 *
 * @author yew_mentzaki & whizzpered
 */
public class JMusicManager {

    static ArrayList<JLoadTask> load() {
        ArrayList<JLoadTask> jlt = getTasks(new File("res/music"));
        return jlt;
    }

    private static ArrayList<JLoadTask> getTasks(File file) {
        File[] files = file.listFiles();
        final ArrayList<JLoadTask> jlt = new ArrayList<JLoadTask>();

        for (File f : files) {
            if (f.isDirectory()) {
                for (JLoadTask task : getTasks(f)) {
                    jlt.add(task);
                }
            } else {
                final File imageFile = f;

                jlt.add(new JLoadTask("Loading music \"" + imageFile.getAbsolutePath().replace(new File("res/music").getAbsolutePath() + System.getProperty("file.separator"), "") + "\"...") {

                    @Override
                    void load() {
                        loadSong(imageFile);
                    }
                });
            }
        }
        songs = new Music[jlt.size()];
        names = new String[jlt.size()];
        ArrayList<JLoadTask> ret = new ArrayList<JLoadTask>();
        int index_of_sound = r.nextInt(jlt.size());
        ret.add(jlt.get(index_of_sound));
        jlt.remove(index_of_sound);
        new Thread("Music loader") {

            @Override
            public void run() {
                for (JLoadTask lt : jlt) {
                    lt.load();
                }
            }

        }.start();
        
        return ret;
    }

    private static int number = 0;
    private static Music[] songs;
    private static String[] names;
    private static Music nowPlaying;
    private static int indexOfPlaying;
    private static Random r = new Random();

    public static void start() {
        if (songs.length == 0) {
            return;
        }
        indexOfPlaying = 0;
        nowPlaying = songs[indexOfPlaying];
        nowPlaying.setVolume((float) (Twisc.settings.get("music").getValueAsDouble()) / 100.0f);

        nowPlaying.setPosition(0.01f);
        nowPlaying.play();

        Thread musicThread = new Thread("Music Thread") {

            @Override
            public void run() {

                while (true) {
                    if (nowPlaying.getPosition() > 0.0) {
                        nowPlaying.setVolume((float) (Twisc.settings.get("music").getValueAsDouble()) / 100.0f);
                    } else {
                        int i = r.nextInt(songs.length);

                        while (i == indexOfPlaying) {

                            i = r.nextInt(songs.length);
                        }
                        indexOfPlaying = i;
                        nowPlaying = songs[indexOfPlaying];
                        nowPlaying.play();
                        nowPlaying.setPosition(0.01f);
                    }
                }

            }

        };
        musicThread.setPriority(Thread.MIN_PRIORITY);
        musicThread.setDaemon(true);
        musicThread.start();
    }

    private static void loadSong(File image) {
        String shortName = image.getAbsolutePath().replace(new File("res/music").getAbsolutePath() + System.getProperty("file.separator"), "");
        String format = shortName.substring(shortName.lastIndexOf(".") + 1, shortName.length()).toUpperCase();
        try {
            songs[number] = new Music(new FileInputStream(image), ".ogg");
            names[number] = shortName;
        } catch (Exception ex) {
            System.out.println(image.getAbsoluteFile());
        }
        number++;
    }
}
