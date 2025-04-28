// ai/AIControl.java
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

            if (distanceToObstacle > 0 && distanceToObstacle < 120) { 
                if (player.isOnGround()) {
                    player.jump(); // Normal jump
                    return;
                }
                else if (player.isJumping()) {
                    player.jump(); // Second/third jump if needed
                    return;
                }
            }
        }
    }
}
