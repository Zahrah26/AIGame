
// model/Player.java
package game.model;

import game.utils.Constants;
import game.utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The Player class defines the main character controlled either by the user or AI.
 * It supports vertical movement (jumping), power-up growth, temporary invincibility (revive),
 * and rendering logic. This class encapsulates player-specific behaviors and attributes.
 */

public class Player {

    private int x, y;                      // Player's current position on screen
    private int width = 60, height = 80;   // Default size of the player
    private int velocityY = 0;             // Vertical speed component for jumping
    private boolean inAir = false;         // Whether the player is currently airborne
    private int jumps = 0;                 // Number of jumps made (supports up to triple jump)

    private Image sprite;                  // Image to visually represent the player

    private boolean isInvisible = false;  // Used during revive (grants temporary invincibility)
    private long invisibleStartTime;      // Time revive effect began
    private boolean isPoweredUp = false;  // For power-up (grow size)
    private long powerUpStartTime;        // Timestamp for power-up duration tracking

     /**
     * Constructor that initializes a player at a specific starting position.
     * Attempts to load a sprite image from the assets folder.
     *
     * Parameter x: The initial horizontal position
     * Parameter y: The initial vertical position
     */
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    // Attempt to load the player's image from disk
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

/**
     * Updates the player’s vertical position and handles power-up and revive timeouts.
     * Also simulates gravity and handles landing logic.
     */
    public void update() {

         // Apply gravity when the player is in mid-air
        if (inAir) {
            velocityY += Constants.GRAVITY; // Gravity
            y += velocityY;

            if (y >= Constants.GROUND_Y) { // Ground level
                y = Constants.GROUND_Y;
                velocityY = 0;
                inAir = false;
                jumps = 0;                 // Reset jump count on ground
            }
        }

        // Handle invisibility timeout after revive - disable invisibility after 1 second of revive
        if (isInvisible && System.currentTimeMillis() - invisibleStartTime > 1000) {
            isInvisible = false;
        }

        // Handle power-up timeout - disable power-up after 5 seconds, and reset player size
        if (isPoweredUp && System.currentTimeMillis() - powerUpStartTime > 5000) {
            isPoweredUp = false;
            width = 60;
            height = 80;
        }
    }

    // User-controlled jump
     /**
     * Performs a jump if jump count is within limit.
     * This version is used when the user presses the jump key.
     */
    public void jump() {
        if (jumps < Constants.MAX_JUMPS) {
            velocityY = -20;
            inAir = true;
            jumps++;
            SoundPlayer.play("assets/jump.wav");
        }
    }

    // AI-controlled jump
    /**
     * Performs a jump initiated by the AI.
     * Mirrors user jump logic but triggered via code(automatically).
     */
    public void ai_jump() {
        if (jumps < Constants.MAX_JUMPS) {
            velocityY = -20;
            inAir = true;
            jumps++;
            SoundPlayer.play("assets/jump.wav");
        }
    }

    /**
     * Optional dodge function, currently reuses the jump sound as placeholder.
     */
    public void dodge() {
        SoundPlayer.play("assets/jump.wav"); 
    }

    /**
     * Plays a victory sound when the game is won.
     */
    public void celebrate() {
        SoundPlayer.play("assets/win.wav"); // Play victory sound
    }

    /**
     * Triggers revive by making the player invisible for a short time (1s).
     * Used after losing a life to prevent instant further collisions.
     */
    public void revive() {
        isInvisible = true;
        invisibleStartTime = System.currentTimeMillis();
    }

    /**
     * Activates a power-up by enlarging the player temporarily.
     */
    public void powerUp() {
        isPoweredUp = true;
        powerUpStartTime = System.currentTimeMillis();
        width = 80; // Grow bigger during power-up
        height = 100;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public boolean isOnGround() {
        return y >= Constants.GROUND_Y;
    }

    public boolean isJumping() {
        return inAir;
    }

     /**
     * check whether the player is near goal.
     */
    public boolean isNearGoal() {
        return false; 
    }

     /**
     * Renders the player on the screen using the Graphics object.
     * If invisible, no drawing occurs.
     *
     * Parameter g - Graphics context for drawing
     */
    public void draw(Graphics g) {
        if (isInvisible) return; // Do not draw player if invisible (revive effect)

        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    /**
     * Returns the player’s hitbox used for collision detection.
     *
     * Return a rectangle around the player
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

