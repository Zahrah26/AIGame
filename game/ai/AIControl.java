package game.ai;

import java.util.List;

import game.model.Obstacle;
import game.model.Player;

/**
 * The AIControl class handles decision-making for the AI-controlled player.
 * It scans the list of obstacles and triggers a jump if an obstacle is near.
 */
public class AIControl {
    private Player player; // The player instance controlled by the AI

    /**
     * Constructor that links a player instance to this AI controller.
     * 
     * Parameter player: The player object that will be controlled by the AI
     */

    public AIControl(Player player) {
        this.player = player;
    }

     /**
     * Called during each game update to determine whether the AI-controlled
     * player should perform an action, such as jumping.
     * 
     * Parameter obstacles: A list of active obstacles currently on the screen
     */

    public void update(List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
             // Calculate the horizontal distance between the obstacle and the player
            double distance = obstacle.getX() - player.getX();

            // Check if the obstacle is in front of the player and within jump-trigger range
            if (distance > 0 && distance < 150) {

                 // If the player is on the ground or already in the air (for multi-jump),
                // trigger the AI-controlled jump. This supports up to 3 jumps if allowed.
                if (player.isOnGround() || player.isJumping()) {
                    player.ai_jump(); // AI jump uses the same logic as user-triggered jump
                    return; // Exit after the first valid jump to avoid multiple jumps in one update cycle
                }
            }

            // NOTE: The block below is a previous alternative implementation and has been commented out.
            // It checked for a smaller distance (100) and only allowed jumping when the player is on the ground.
            // That logic could restrict the AI from using double/triple jumps. 

              /* if(obstacle.getX() - player.getX() < 100 && player.isOnGround()){
			 	player.jump();
			 	break;
			}   
               */
        }
            
    }
}



