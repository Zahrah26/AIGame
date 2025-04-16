package ai;

import java.util.List;
import model.Obstacle;
import model.Player;

public class AIControl {
    private Player player;

    public AIControl(Player player) {
        this.player = player;
    }

    public void update(List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            double distanceToObstacle = obstacle.getX() - player.getX();

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
    }
}
