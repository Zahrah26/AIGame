package ai;

import java.util.List;
import model.Obstacle;
import model.Player;

public class AIControl {
    private Player player;

    // Life counter: player starts with 3 lives
    private int lives = 3;

    // Game over flag to stop actions once lives reach 0
    private boolean gameOver = false;

    public AIControl(Player player) {
        this.player = player;
    }

    public void update(List<Obstacle> obstacles) {
        // Stop updating if game is already over
        if (gameOver) {
            System.out.println("Game Over! No lives left.");
            return;
        }

        for (Obstacle obstacle : obstacles) {
            double distanceToObstacle = obstacle.getX() - player.getX();

            // Simulated collision detection: If too close and not jumping, consider it a hit
            if (Math.abs(distanceToObstacle) < 5 && !player.isJumping()) {
                // Deduct one life on collision
                lives--;

                // Notify remaining lives
                System.out.println("Collision detected! Lives remaining: " + lives);

                // If all lives are lost, mark game as over
                if (lives <= 0) {
                    gameOver = true;
                    System.out.println("Game Over! You lost all your lives.");
                }

                // End this update cycle after a hit
                return;
            }

            // Jump if the obstacle is near and the player is on the ground
            if (distanceToObstacle < 100 && distanceToObstacle > 0 && player.isOnGround()) {
                player.jump();
            }

            // Try double jump if close and mid-air
            if (distanceToObstacle < 50 && !player.isOnGround()) {
                player.doubleJump(); 
            }

            // If near the goal, trigger a celebration
            if (player.isNearGoal()) {
                player.celebrate();
                return;
            }
        }

        // Display current lives during normal gameplay
        System.out.println("Lives remaining: " + lives);
    }
}
