package game.utils;

/**
 * The Constants class contains static values that define core physics rules and gameplay limits.
 * These constants are used throughout the game to ensure consistency and easy configuration.
 */
public class Constants {

     /**
     * The fixed vertical position (in pixels) that represents the ground level in the game world.
     * When the player is standing on the ground, their Y-coordinate will match this value.
     * This value is used for jump detection and landing logic.
     */
    public static final int GROUND_Y = 500;     

    /**
     * The gravitational acceleration applied to the player while in the air.
     * Each frame, this value is added to the player’s vertical velocity to simulate free fall.
     * Higher values make falling faster and jumps sharper.
     */
    public static final int GRAVITY = 1;         
    
     /**
     * The maximum number of consecutive jumps the player is allowed to perform before touching the ground.
     * This allows for enhanced gameplay mechanics such as double or triple jumps.
     * For example, MAX_JUMPS = 3 enables a triple jump mechanic.
     */
    public static final int MAX_JUMPS = 3;      
}
