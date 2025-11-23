package org.dbs.robot.driver.arduino.serial;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface to abstract SerialPort functionality for better testability.
 * This wrapper allows us to mock serial port operations in tests.
 */
public interface SerialPortWrapper {
    // Timeout modes from SerialPort
    int TIMEOUT_READ_SEMI_BLOCKING = 8;
    /**
     * Gets the system port name.
     *
     * @return The system port name
     */
    String getSystemPortName();

    /**
     * Gets the descriptive port name.
     *
     * @return The descriptive port name
     */
    String getDescriptivePortName();

    /**
     * Sets the baud rate for the serial port.
     *
     * @param baudRate The baud rate to set
     * @return true if successful, false otherwise
     */
    boolean setBaudRate(int baudRate);

    /**
     * Sets the timeouts for the serial port.
     *
     * @param mode         The timeout mode
     * @param readTimeout  The read timeout in milliseconds
     * @param writeTimeout The write timeout in milliseconds
     */
    void setComPortTimeouts(int mode, int readTimeout, int writeTimeout);

    /**
     * Opens the serial port.
     *
     * @return true if successful, false otherwise
     */
    boolean openPort();

    /**
     * Checks if the serial port is open.
     *
     * @return true if open, false otherwise
     */
    boolean isOpen();

    /**
     * Gets the input stream for the serial port.
     *
     * @return The input stream
     */
    InputStream getInputStream();

    /**
     * Gets the output stream for the serial port.
     *
     * @return The output stream
     */
    OutputStream getOutputStream();

    /**
     * Closes the serial port.
     *
     * @return true if successful, false otherwise
     */
    boolean closePort();
}