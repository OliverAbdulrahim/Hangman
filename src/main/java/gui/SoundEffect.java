package gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundEffect {

    private Thread thread;
    private static boolean isMute;
    private static SoundEffect music;

    //audio of each object stored here
    private Clip sound;


    public SoundEffect(String path) {
        this(path, false);
    }

    private SoundEffect(String path, boolean isRepeating) {
        sound = getClip(path, isRepeating);
    }

    private static Clip getClip(String path, boolean isRepeating) {
            Clip c = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    SoundEffect.class.getResourceAsStream(path));
            c = AudioSystem.getClip();
            c.open(ais);
            if (isRepeating) {
                c.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch (LineUnavailableException | IOException
                | UnsupportedAudioFileException ex)
        {
            Logger.getLogger(Hangman_GUI.class.getName()).log(Level.SEVERE,"", ex);
            System.out.println("Probably wrong path.");
        }
        return c;
    }

    public void start() {
        if (!isMute) {
            if (this != music) {
                thread = new Thread(() -> {
                    sound.start();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    do {
                        if (isMute) {
                            sound.stop();
                        }
                    }while(sound.isActive());
                });
                thread.start();

            } else {
                this.sound.loop(Clip.LOOP_CONTINUOUSLY);
                sound.start();
            }
        }
    }

    public static void stop(SoundEffect effect) {
        effect.stop();
    }

    private void stop() {
        sound.stop();
    }

    public static void mute() {
            try {
                isMute = true;
                music.sound.stop();
            } catch (Exception ex) {System.out.println("I don't know why would this happen anyways.");}
    }

    public static void unmute () {
            try {
                isMute = false;
                music.sound.start();
            } catch (Exception ex) {System.out.println("I don't know why would this happen anyways.");}
        }
    public static void setMusic(String path){
        music = new SoundEffect(path, true);
    }
    public static SoundEffect getMusic(){
        return music;
    }
    public static boolean isMute(){
        return isMute;
    }
}
