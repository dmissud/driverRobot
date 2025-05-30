package org.dbs.robot.driverrobot.arduino;

/**
 * Composite interface for controlling an Arduino with LEDs and servomotors.
 * This interface combines LED control, servo control, and lifecycle management.
 */
public interface ArduinoController extends LedController, ServoController, ControllerLifecycle {
    // This interface inherits all methods from the parent interfaces
}