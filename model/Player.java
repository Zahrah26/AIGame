package model;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Player {
    private int x, y;
    private int width = 60, height = 80;
    private int velocityY = 0;
    private boolean jumping = false;
    private Image sprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        try {
            File imgFile = new File("assets/player.png");
            if (imgFile.exists()) {
                sprite = new ImageIcon(imgFile.getAbsolutePath()).getImage();
            } else {
                System.out.println("Player image not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (jumping) {
            velocityY += 1;
            y += velocityY;
            if (y >= 500) {
                y = 500;
                velocityY = 0;
                jumping = false;
            }
        }
    }

    public void jump() {
        if (!jumping) {
            jumping = true;
            velocityY = -20;
            utils.SoundPlayer.play("assets/jump.wav"); // 🔊 Jump sound
        }
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}
