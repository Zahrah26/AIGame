package utils;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    public static void play(String soundFile) {
        try {
            File file = new File(soundFile);
            if (!file.exists()) {
                System.out.println("Sound file not found: " + soundFile);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
