package game.engine;

import javax.swing.*;

/**
 * Main class: Entry point of the AI Obstacle Dodger game.
 * Allows the player to choose between AI-controlled mode or user-controlled mode,
 * sets up the game window in full screen, and starts the game.
 */
public class Main {
    public static boolean aiMode = true; // true = AI Mode, false = User Mode

    public static void main(String[] args) {
 // All Swing UI updates and game startup are done in the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {

             // Show a dialog box to let the player choose the game mode (AI or User)
            int mode = JOptionPane.showOptionDialog(
                null, // Parent component (none)
                "Choose Game Mode",// Dialog message
                "Game Mode Selection",// Dialog title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Icon type
                null,// No custom icon
                new String[]{"AI Mode", "User Mode"},// Options displayed
                "AI Mode"// Default selected option
            );

             // Store the selected mode in aiMode: AI if option 0, User if option 1
            aiMode = (mode == 0); 

            // Create the main game window
            JFrame frame = new JFrame("MoonWalk- AI Jump Rush");
            
            // Close the application when the game window is closed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Remove window borders and title bar for immersive experience
            frame.setUndecorated(true);
            
            // Maximize the window to occupy the entire screen
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

// Create the game panel where the game runs and add it to the frame
            GamePanel panel = new GamePanel();
            frame.add(panel);

            // Display the game window
            frame.setVisible(true);

             // Start the actual game loop
            panel.startGame();
        });
    }
}
