package game.utils;

import javax.sound.sampled.*;
import java.io.File;

/**
 * SoundPlayer is a utility class responsible for handling audio playback during the game.
 * It plays short sound effects such as jumping, winning, and game-over notifications.
 * Java's Clip and AudioSystem classes from the javax.sound.sampled package are used for synchronous, low-latency audio playback.
 */
public class SoundPlayer {

    /**
     * Plays a sound effect from the specified audio file path.
     */

    public static void play(String soundFile) {
        try {

            // Create a File object from the provided path
            File file = new File(soundFile);

            // Ensure the file exists before attempting to play it
            if (!file.exists()) {
                System.out.println("Sound file not found: " + soundFile);
                return;     // Exit method early if file is missing
            }

            // Convert the audio file into an input stream for processing
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();

            // Load audio data from the stream into the clip
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {

             // Handle and log any exceptions ( IO errors, unsupported formats, etc.)
            System.out.println("Sound error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
