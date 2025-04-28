package ai;

import java.util.List;
import model.Obstacle;
import model.Player;
import utils.SoundPlayer;

public class AIControl {
    private Player player;
    private int lives = 3;
    private boolean gameOver = false;

    public AIControl(Player player) {
        this.player = player;
    }

    public void update(List<Obstacle> obstacles) {
        if (gameOver) {
            System.out.println("Game Over! No lives left.");
            return;
        }

        for (Obstacle obstacle : obstacles) {
            double distanceToObstacle = obstacle.getX() - player.getX();

            // Predict collision based on time-to-collision
            if (distanceToObstacle > 0 && distanceToObstacle < 100) {
                int relativeSpeed = obstacle.getSpeed();
                double timeToCollision = distanceToObstacle / relativeSpeed;

                // Emergency double-jump if very close
                if (timeToCollision < 0.5 && player.isJumping()) {
                    player.doubleJump();
                }

                // Normal jump if close and on ground
                else if (timeToCollision < 1.5 && player.isOnGround()) {
                    player.jump();
                }
            }

            // Special logic for very fast obstacles
            if (distanceToObstacle < 50 && obstacle.getSpeed() > 5) {
                player.dodge();
            }

            // Simulated collision check (if AI fails to react)
            if (Math.abs(distanceToObstacle) < 5 && !player.isJumping()) {
                lives--;
                System.out.println("Collision detected! Lives remaining: " + lives);
                SoundPlayer.play("assets/gameover.wav");

                if (lives <= 0) {
                    gameOver = true;
                    System.out.println("Game Over! You lost all your lives.");
                    SoundPlayer.play("assets/gameover.wav");
                }

                return; // stop processing this frame
            }

            // Trigger celebration if near goal
            if (player.isNearGoal()) {
                player.celebrate();
                return;
            }
        }

        System.out.println("AIControl running | Lives: " + lives);
    }
}
