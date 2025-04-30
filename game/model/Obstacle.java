// model/Obstacle.java
package game.model;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

/** 
 * The Obstacle class represents moving barriers in the game.
 * Each obstacle travels from right to left across the screen
 * and is used to challenge the player’s reflexes and decision-making.
 */

public class Obstacle {

    // Position coordinates of the obstacle 
    private int x, y;

    // Dimensions of the obstacle; can be adjusted 
   // private int width = 30, height = 45;  // smaller obstacle
    private int width = 50, height = 100;

    // Speed at which the obstacle moves toward the player
    private int speed;

     // Sprite image used to visually represent the obstacle
    private Image sprite;

    /**
     * Constructs a new Obstacle object with a randomized speed.
     * Initializes its position and attempts to load a graphical image.
     *
     * parameter x- The initial x-coordinate where the obstacle appears (usually off-screen to the right)
     * parameter y- The y-coordinate (ground level alignment)
     */

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;

    // Assign a random speed between 5 and 12 to increase gameplay variation
        this.speed = new Random().nextInt(8) + 5; 
    
    // Load the obstacle sprite from the assets directory
        try {
            File imgFile = new File("assets/obstacle.png");
            if (imgFile.exists()) {
                sprite = new ImageIcon(imgFile.getAbsolutePath()).getImage();
            } else {
                System.out.println("Obstacle image not found.");
            }
        } catch (Exception e) {
    // Log any exception that might occur during sprite loading
            e.printStackTrace();
        }
    }

    /**
     * Updates the x-position of the obstacle to simulate movement.
     * This method is called continuously in the game loop.
     */

    public void update() {
        x -= speed; // Move obstacle leftward by its speed value
    }

/**
     * Renders the obstacle on the game screen using the provided Graphics object.
     * If the image is unavailable, a red rectangle is drawn instead.
     *
     * parameter g-The graphics context used to render the object
     */
    public void draw(Graphics g) {
        if (sprite != null) {
            // Draw the obstacle using its sprite image
            g.drawImage(sprite, x, y, width, height, null);
        } else {

    // Fallback: draw a solid red rectangle if sprite is missing
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }

        // Optional: display speed value for debugging
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Speed: " + speed, x, y - 10);
    }
    /**
     * Returns a Rectangle object representing the obstacle’s hitbox.
     * Used for collision detection with the player.
     *
     * return A rectangle covering the obstacle’s area
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns the current x-coordinate of the obstacle.
     *
     * Return the horizontal position on screen
     */

    public int getX() {
        return x;
    }

    /**
     * Returns the width of the obstacle, useful for off-screen checks.
     *
     * Return the obstacle's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the speed of the obstacle (how fast it moves left).
     *
     * return the obstacle's speed value
     */

    public int getSpeed() {
        return speed;
    }
}
