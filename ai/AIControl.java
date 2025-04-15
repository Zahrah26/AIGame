package ai;

import model.Player;
import model.Obstacle;

import java.util.List;

public class AIControl {
    private Player player;
    private List<Obstacle> obstacles;

    public AIControl(Player player, List<Obstacle> obstacles) {
        this.player = player;
        this.obstacles = obstacles;
    }

    public void makeDecision() {
        for (Obstacle obs : obstacles) {
            int distance = obs.getX() - 100;
            if (distance > 0 && distance < 150 && player.getY() >= 500) {
                player.jump();
                break;
            }
        }
    }
}

