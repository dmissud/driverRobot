package org.dbs.robot.driver.arduino;

/**
 * Interface for controlling the position of servomotors connected to an Arduino.
 * Commands are sent in the format: servo(name, angle X)\n
 * where X is the desired angle
 */
public interface ServoPositionController {
    /**
     * Positions a specific servomotor to a given angle.
     *
     * @param name  The name identifier of the servomotor
     * @param angle The angle to position the servomotor (typically 0-180 degrees)
     * @return true if the operation was successful, false otherwise
     */
    boolean positionServo(String name, int angle);
}