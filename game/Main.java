// game/Main.java
package game;

import javax.swing.*;

public class Main {
    public static boolean aiMode = true; // true = AI Mode, false = User Mode

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int mode = JOptionPane.showOptionDialog(
                null,
                "Choose Game Mode",
                "Game Mode Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"AI Mode", "User Mode"},
                "AI Mode"
            );

            aiMode = (mode == 0); // 0 = AI Mode, 1 = User Mode

            JFrame frame = new JFrame("AI Obstacle Dodger");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            GamePanel panel = new GamePanel();
            frame.add(panel);
            frame.setVisible(true);
            panel.startGame();
        });
    }
}
