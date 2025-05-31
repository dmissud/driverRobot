package org.dbs.robot.driver.arduino;

/**
 * Composite interface for controlling servomotors connected to an Arduino.
 * This interface combines both positioning and movement capabilities.
 */
public interface ServoController extends ServoPositionController, ServoMovementController {
    // This interface inherits all methods from the parent interfaces
}