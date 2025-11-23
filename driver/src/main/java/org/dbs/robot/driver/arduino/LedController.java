package org.dbs.robot.driver.arduino;

/**
 * Interface for controlling LEDs connected to an Arduino.
 * Commands are sent in the format: led(name, state)\n
 * where state is either "on" or "off"
 */
public interface LedController {
    /**
     * Controls the state of a specific LED.
     *
     * @param name  The name identifier of the LED
     * @param state true to turn the LED on, false to turn it off
     * @return true if the operation was successful, false otherwise
     */
    boolean controlLed(String name, boolean state);
}