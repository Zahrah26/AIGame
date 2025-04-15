package model;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Obstacle {
    private int x, y;
    private int width = 50, height = 70;
    private int speed = 8;
    private Image sprite;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;

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
}
