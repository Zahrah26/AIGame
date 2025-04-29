
// model/Player.java
package model;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Player {
    private int x, y;
    private int width = 60, height = 80;
    private int velocityY = 0;
    private boolean inAir = false;
    private int jumps = 0;
    private final int MAX_JUMPS = 3; // Allow triple jumps
    private Image sprite;

    private boolean isInvisible = false; // For revive (temporary invincibility)
    private long invisibleStartTime;
    private boolean isPoweredUp = false; // For power-up (grow size)
    private long powerUpStartTime;

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
        if (inAir) {
            velocityY += 1; // Gravity
            y += velocityY;

            if (y >= 500) { // Ground level
                y = 500;
                velocityY = 0;
                inAir = false;
                jumps = 0; // Reset jump count on ground
            }
        }

        // Handle invisibility timeout after revive
        if (isInvisible && System.currentTimeMillis() - invisibleStartTime > 1000) {
            isInvisible = false;
        }

        // Handle power-up timeout
        if (isPoweredUp && System.currentTimeMillis() - powerUpStartTime > 5000) {
            isPoweredUp = false;
            width = 60;
            height = 80;
        }
    }

    // User-controlled jump
    public void jump() {
        if (jumps < MAX_JUMPS) {
            velocityY = -20;
            inAir = true;
            jumps++;
            utils.SoundPlayer.play("assets/jump.wav");
        }
    }

    // AI-controlled jump
    public void ai_jump() {
        if (jumps < MAX_JUMPS) {
            velocityY = -20;
            inAir = true;
            jumps++;
            utils.SoundPlayer.play("assets/jump.wav");
        }
    }

    public void dodge() {
        utils.SoundPlayer.play("assets/jump.wav"); // Optional: reuse jump sound for dodge
    }

    public void celebrate() {
        utils.SoundPlayer.play("assets/win.wav"); // Play victory sound
    }

    public void revive() {
        isInvisible = true;
        invisibleStartTime = System.currentTimeMillis();
    }

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
        return y >= 500;
    }

    public boolean isJumping() {
        return inAir;
    }

    public boolean isNearGoal() {
        return false; // Placeholder
    }

    public void draw(Graphics g) {
        if (isInvisible) return; // Don't draw player if invisible (revive effect)

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

