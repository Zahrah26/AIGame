
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
            double distance = obstacle.getX() - player.getX();

            if (distance > 0 && distance < 150) {
                if (player.isOnGround() || player.isJumping()) {
                    player.ai_jump(); // jump immediately if possible (up to 3 times)
                    return;
                }
            }
        }
    }
}

