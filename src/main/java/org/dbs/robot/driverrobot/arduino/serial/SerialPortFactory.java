package org.dbs.robot.driverrobot.arduino.serial;

/**
 * Interface to abstract static methods of SerialPort for better testability.
 * This factory allows us to mock static methods in tests.
 */
public interface SerialPortFactory {
    /**
     * Gets all available serial ports.
     *
     * @return Array of SerialPortWrapper objects representing available ports
     */
    SerialPortWrapper[] getCommPorts();
}