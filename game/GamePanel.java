package game;

import ai.AIControl;
import model.Player;
import model.Obstacle;
import utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private AIControl ai;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private Image background;
    private final int WIN_SCORE = 1500;

    public GamePanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(Color.BLACK);

        try {
            File bgFile = new File("assets/background.png");
            if (bgFile.exists()) {
                background = new ImageIcon(bgFile.getAbsolutePath()).getImage();
            } else {
                System.out.println("Background image not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        player = new Player(100, 500);
        obstacles = new ArrayList<>();
        ai = new AIControl(player, obstacles);
        timer = new Timer(20, this);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !gameWin) {
            ai.makeDecision();
            player.update();

            for (Obstacle obs : obstacles) obs.update();

            checkCollisions();
            spawnObstacles();
            cleanupObstacles();

            score++;
            if (score >= WIN_SCORE) {
                gameWin = true;
                SoundPlayer.play("assets/win.wav"); // 🔊 Win sound
                timer.stop();
            }
            repaint();
        }
    }

    private void checkCollisions() {
        for (Obstacle obs : obstacles) {
            if (player.getBounds().intersects(obs.getBounds())) {
                gameOver = true;
                SoundPlayer.play("assets/gameover.wav"); // 🔊 Game Over sound
                timer.stop();
            }
        }
    }

    private void spawnObstacles() {
        if (new Random().nextInt(100) < 3) {
            obstacles.add(new Obstacle(getWidth(), 500));
        }
    }

    private void cleanupObstacles() {
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            if (obs.getX() + obs.getWidth() < 0) it.remove();
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

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 30, 50);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 72));
            g.drawString("GAME OVER", getWidth() / 2 - 200, getHeight() / 2);
        }

        if (gameWin) {
            g.setFont(new Font("Arial", Font.BOLD, 72));
            g.drawString("YOU WIN!", getWidth() / 2 - 180, getHeight() / 2);
        }
    }
}
