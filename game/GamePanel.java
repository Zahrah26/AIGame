// game/GamePanel.java
package game;

import ai.AIControl;
import model.Obstacle;
import model.Player;
import utils.SoundPlayer;

import javax.swing.Timer;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Point> tokens;
    private AIControl ai;
    private int score = 0;
    private int scoreCounter = 0;
    private int highScore = 0;
    private int lives = 3;
    private int tokenCount = 0;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private boolean showGameOverScreen = false;
    private Image background;
    private Image coinImg;
    private final int WIN_SCORE = 2000;
    private final int TOKEN_POWERUP = 5;
    private final String SCORE_FILE = "score.txt";

    public GamePanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(Color.BLACK);

        try {
            File bgFile = new File("assets/background.png");
            if (bgFile.exists()) background = new ImageIcon(bgFile.getAbsolutePath()).getImage();

            File coinFile = new File("assets/coin.png");
            if (coinFile.exists()) coinImg = new ImageIcon(coinFile.getAbsolutePath()).getImage();
            else System.out.println("Coin image not found!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadHighScore();
        initGame();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!Main.aiMode && e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver && !gameWin) {
                    player.jump();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });
    }

    private void initGame() {
        player = new Player(100, 500);
        obstacles = new ArrayList<>();
        tokens = new ArrayList<>();
        ai = new AIControl(player);
        score = 0;
        scoreCounter = 0;
        lives = 3;
        tokenCount = 0;
        gameOver = false;
        gameWin = false;
        showGameOverScreen = false;
        timer = new Timer(15, this);
    }

    public void startGame() {
        initGame();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWin) {
            if (Main.aiMode) ai.update(obstacles);
            player.update();

            for (Obstacle obs : obstacles) obs.update();
            checkCollisions();
            spawnObstacles();
            spawnTokens();
            updateTokens();
            cleanupObstacles();
            cleanupTokens();
            checkTokenPickup();

            scoreCounter++;
            if (scoreCounter % 2 == 0) score++;

            if (score >= WIN_SCORE) {
                gameWin = true;
                SoundPlayer.play("assets/win.wav");
                saveHighScore();
                timer.stop();
                showGameOverScreen = true;
            }
        }

        repaint();
    }

    private void checkCollisions() {
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            if (!player.isInvisible() && player.getBounds().intersects(obs.getBounds())) {
                lives--;
                SoundPlayer.play("assets/gameover.wav");
                player.revive();
                it.remove();

                if (lives <= 0) {
                    gameOver = true;
                    saveHighScore();
                    timer.stop();
                    showGameOverScreen = true;
                }
                break;
            }
        }
    }

    private void spawnObstacles() {
        if (new Random().nextInt(300) < 1) { // ✅ Much fewer obstacles (1 in 250 frames)
            int offset = new Random().nextInt(400) + 600; // ✅ Distance between 600 and 1000px
            obstacles.add(new Obstacle(getWidth() + offset, 500));
        }
    }

    private void spawnTokens() {
        if (new Random().nextInt(140) == 0) {
            int tokenY = 510;
            tokens.add(new Point(getWidth(), tokenY));
        }
    }

    private void updateTokens() {
        for (Point token : tokens) {
            token.x -= 4;
        }
    }

    private void checkTokenPickup() {
        Iterator<Point> it = tokens.iterator();
        while (it.hasNext()) {
            Point token = it.next();
            Rectangle tokenRect = new Rectangle(token.x, token.y, 20, 20);
            if (player.getBounds().intersects(tokenRect)) {
                tokenCount++;
                it.remove();

                if (tokenCount >= TOKEN_POWERUP) {
                    lives++;
                    player.powerUp();
                    tokenCount = 0;
                }
            }
        }
    }

    private void cleanupObstacles() {
        obstacles.removeIf(obs -> obs.getX() + obs.getWidth() < 0);
    }

    private void cleanupTokens() {
        tokens.removeIf(token -> token.x < 0);
    }

    private void showEndDialog() {
        String message = gameWin ? "🎉 YOU WIN! " : "💀 GAME OVER! ";
        message += "\nScore: " + score;

        if (score >= highScore) {
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

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) highScore = Integer.parseInt(line);
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        if (score > highScore) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
                writer.write(String.valueOf(score));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        player.draw(g);
        for (Obstacle obs : obstacles) obs.draw(g);

        for (Point token : tokens) {
            if (coinImg != null) {
                g.drawImage(coinImg, token.x, token.y, 20, 20, null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillOval(token.x, token.y, 20, 20);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 30, 50);
        g.drawString("Lives: " + lives + "  Tokens: " + tokenCount + "/5", 30, 80);

        if (showGameOverScreen) {
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(Color.RED);
            g.drawString(gameOver ? "GAME OVER" : "YOU WIN!", getWidth() / 2 - 200, getHeight() / 2);
            showGameOverScreen = false;
            SwingUtilities.invokeLater(this::showEndDialog);
        }
    }
}


