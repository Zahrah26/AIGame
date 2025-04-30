package game.engine;

import game.ai.AIControl;
import game.model.Obstacle;
import game.model.Player;
import game.utils.Constants;
import game.utils.SoundPlayer;

import javax.swing.Timer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * GamePanel is the core game class that handles:
 * - Game logic and loop (via Timer)
 * - Player movement and updates (manual or AI)
 * - Collision detection
 * - Scoring and token collection
 * - Rendering graphics and UI
 */


public class GamePanel extends JPanel implements ActionListener {
    
    // Timer controls the game loop 
    private Timer timer;

    // Main game components
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Point> tokens;
    private AIControl ai;

    private int currentScore = 0;
    private int scoreCounter = 0;
    private int highScore = 0;
    private int playerLives = 3;
    private int tokenCount = 0;

    // Game state flags
    private boolean isGameOver = false;
    private boolean hasWon = false;
    private boolean showGameOverScreen = false;

    // Game visuals
    private Image background;
    private Image coinImg;

    private final int WINNING_SCORE_THRESHOLD = 2000;// Win if this score is reached
    private final int TOKEN_POWERUP = 5;// Gain a life after 5 tokens
    private final String SCORE_FILE_RECORD = "score.txt";// File storing the high score

    /**
     * Constructor: Initializes the game panel, loads assets, sets key listeners, and prepares game.
     */
    public GamePanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
 
        // Load background and token image from file
        try {
            File bgFile = new File("assets/background.png");
            if (bgFile.exists()) {
                background = new ImageIcon(bgFile.getAbsolutePath()).getImage();
            }

            File coinFile = new File("assets/coin.png");
            if (coinFile.exists()) {
                coinImg = new ImageIcon(coinFile.getAbsolutePath()).getImage();
            } else {
                System.out.println("Coin image not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadHighScore();// Load saved high score from file
        initGame();// Set up game variables

        // Keyboard input handling 
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Space = jump (if not in AI mode and game is active)
                if (!Main.aiMode && e.getKeyCode() == KeyEvent.VK_SPACE && !isGameOver && !hasWon) {
                    player.jump();
                }

                // Escape = exit game
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });
    }

    /**
     * Initializes or resets the game.
     */
    private void initGame() {
        player = new Player(100, Constants.GROUND_Y);
        obstacles = new ArrayList<>();
        tokens = new ArrayList<>();
        ai = new AIControl(player);

        currentScore = 0;
        scoreCounter = 0;
        playerLives = 3;
        tokenCount = 0;
        isGameOver = false;
        hasWon = false;
        showGameOverScreen = false;

        timer = new Timer(15, this); // Call actionPerformed() every 15ms
    }

    /**
     * Starts or restarts the game loop.
     */
    public void startGame() {
        initGame();
        timer.start();
    }

    /**
     * This is the game loop - triggered by the Timer repeatedly.
     * It updates the game state and repaints the screen.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !hasWon) {

            // Use AI to update player if AI mode is active
            if (Main.aiMode) ai.update(obstacles);
            player.update(); // Move player based on gravity and jump logic

            // Update all obstacles
            for (Obstacle obs : obstacles) obs.update();

            // Main game logic sequence
            checkCollisions();
            spawnObstacles();
            spawnTokens();
            updateTokens();
            cleanupObstacles();
            cleanupTokens();
            checkTokenPickup();

             // Increment score over time
            scoreCounter++;
            if (scoreCounter % 2 == 0) currentScore++;

            // Check win condition
            if (currentScore >= WINNING_SCORE_THRESHOLD) {
                hasWon = true;
                SoundPlayer.play("assets/win.wav");
                saveHighScore();
                timer.stop();
                showGameOverScreen = true;
            }
        }

        repaint(); // Request a redraw of the screen
    }

    /**
     * Checks collision between the player and any obstacle.
     * If a collision occurs, a life is lost and the player revives.
     * If lives drop to 0, the game ends.
     */
    private void checkCollisions() {
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            if (!player.isInvisible() && player.getBounds().intersects(obs.getBounds())) {
                playerLives--;
                SoundPlayer.play("assets/gameover.wav");
                player.revive();// Temporary invincibility
                it.remove();// Remove the collided obstacle

                if (playerLives<= 0) {
                    isGameOver = true;
                    saveHighScore();
                    timer.stop();
                    showGameOverScreen = true;
                }
                break;
            }
        }
    }

    /**
     * Spawns a new obstacle at a random interval and position.
     */

    private void spawnObstacles() {
        if (new Random().nextInt(400) < 1) {
            int offset = new Random().nextInt(400) + 600;
            obstacles.add(new Obstacle(getWidth() + offset, Constants.GROUND_Y));
        }
    }

     /**
     * Spawns a token randomly.
     */

    private void spawnTokens() {
        if (new Random().nextInt(140) == 0) {
            int tokenY = Constants.GROUND_Y + 10;
            tokens.add(new Point(getWidth(), tokenY));
        }
    }

    /**
     * Moves tokens left across the screen to simulate motion.
     */
    private void updateTokens() {
        for (Point token : tokens) {
            token.x -= 4;
        }
    }

    /**
     * Checks if the player touches a token.
     * If 5 tokens are collected, a life is added and a power-up is activated.
     */
    private void checkTokenPickup() {
        Iterator<Point> it = tokens.iterator();
        while (it.hasNext()) {
            Point token = it.next();
            Rectangle tokenRect = new Rectangle(token.x, token.y, 20, 20);
            if (player.getBounds().intersects(tokenRect)) {
                tokenCount++;
                it.remove();

                if (tokenCount >= TOKEN_POWERUP) {
                    playerLives++;
                    player.powerUp();
                    tokenCount = 0;
                }
            }
        }
    }

    /**
     * Removes obstacles that have moved off-screen (left side).
     */
    private void cleanupObstacles() {
        obstacles.removeIf(obs -> obs.getX() + obs.getWidth() < 0);
    }

    /**
     * Removes tokens that have gone off-screen.
     */
    private void cleanupTokens() {
        tokens.removeIf(token -> token.x < 0);
    }

     /**
     * Shows end game message with score and high score.
     * Allows user to replay or quit.
     */
    private void showEndDialog() {
        String message = hasWon ? "🎉 YOU WIN! " : "💀 GAME OVER! ";
        message += "\nScore: " + currentScore;

        if (currentScore >= highScore) {
            message += "\n🏆 NEW HIGH SCORE!";
        } else {
            message += "\nHigh Score: " + highScore;
        }

        int option = JOptionPane.showOptionDialog(
            this,
            message + "\nPlay Again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Replay", "Quit"},
            "Replay"
        );

        if (option == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Loads the saved high score from file.
     */

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE_RECORD))) {
            String line = reader.readLine();
            if (line != null) highScore = Integer.parseInt(line);
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    /**
     * Saves the high score to file if the current score beats it.
     */
    private void saveHighScore() {
        if (currentScore > highScore) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_RECORD))) {
                writer.write(String.valueOf(currentScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles all drawing: background, player, obstacles, tokens, HUD, and end screen text.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw game objects
        player.draw(g);
        for (Obstacle obs : obstacles) obs.draw(g);

        // Draw coins or fallback
        for (Point token : tokens) {
            if (coinImg != null) {
                g.drawImage(coinImg, token.x, token.y, 20, 20, null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillOval(token.x, token.y, 20, 20);
            }
        }

        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + currentScore, 30, 50);
        g.drawString("Lives: " + playerLives + "  Tokens: " + tokenCount + "/5", 30, 80);

          // Draw win/lose message
        if (showGameOverScreen) {
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(Color.RED);
            g.drawString(isGameOver ? "GAME OVER" : "YOU WIN!", getWidth() / 2 - 200, getHeight() / 2);
            showGameOverScreen = false;
            SwingUtilities.invokeLater(this::showEndDialog);
        }
    }
}
