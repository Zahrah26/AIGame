// model/Obstacle.java
package model;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

public class Obstacle {
    private int x, y;
    private int width = 50, height = 70;
    private int speed;
    private Image sprite;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = new Random().nextInt(8) + 5; // Speed between 3 and 7

        try {
            File imgFile = new File("assets/obstacle.png");
            if (imgFile.exists()) {
                sprite = new ImageIcon(imgFile.getAbsolutePath()).getImage();
            } else {
                System.out.println("Obstacle image not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }

        // Optional: display speed value for debugging
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Speed: " + speed, x, y - 10);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getSpeed() {
        return speed;
    }
}
