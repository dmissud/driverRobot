package org.dbs.robot.driverrobot.arduino;

/**
 * Interface for controlling complex movements of servomotors connected to an Arduino.
 * Commands are sent in the format: servo(name, movement startAngle endAngle speed)\n
 * where movement is one of: sweep, half-sweep, reverse-half-sweep, reverse-sweep
 */
public interface ServoMovementController {
    /**
     * Performs a complete back-and-forth sweep movement on a servomotor.
     * The servo moves from startAngle to endAngle and back to startAngle.
     *
     * @param name       The name identifier of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep (higher values mean faster movement)
     * @return true if the operation was successful, false otherwise
     */
    boolean sweep(String name, int startAngle, int endAngle, int speed);
    
    /**
     * Performs a half sweep movement on a servomotor in clockwise direction.
     * The servo moves from startAngle to endAngle only.
     *
     * @param name       The name identifier of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep (higher values mean faster movement)
     * @return true if the operation was successful, false otherwise
     */
    boolean halfSweep(String name, int startAngle, int endAngle, int speed);
    
    /**
     * Performs a half sweep movement on a servomotor in counter-clockwise direction.
     * The servo moves from startAngle to endAngle only.
     *
     * @param name       The name identifier of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep (higher values mean faster movement)
     * @return true if the operation was successful, false otherwise
     */
    boolean reverseHalfSweep(String name, int startAngle, int endAngle, int speed);
    
    /**
     * Performs a complete back-and-forth sweep movement on a servomotor in reverse direction.
     * The servo moves from startAngle to endAngle and back to startAngle.
     *
     * @param name       The name identifier of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep (higher values mean faster movement)
     * @return true if the operation was successful, false otherwise
     */
    boolean reverseSweep(String name, int startAngle, int endAngle, int speed);
}