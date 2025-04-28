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

            // Start jumping early and continue jumping if close and in air
            if (distance > 0 && distance < 180) {
                player.jump(); // handles 1st, 2nd, and 3rd jump internally
                return;
            }
        }
    }
}
