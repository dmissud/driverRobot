package org.dbs.robot.driverrobot.arduino.serial;

import lombok.extern.slf4j.Slf4j;
import org.dbs.robot.driverrobot.arduino.config.ArduinoConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for handling serial communication with the Arduino.
 * This class encapsulates all serial port operations, including initialization,
 * sending commands, and reading responses.
 */
@Slf4j
public class SerialCommunicator {

    private static final int COMMAND_TIMEOUT_MS = 5000;
    private static final long COMMAND_DELAY_MS = 200;

    private final String portName;
    private final int baudRate;
    private final SerialPortFactory serialPortFactory;
    private SerialPortWrapper serialPort;
    private BufferedReader reader;
    private OutputStream outputStream;

    /**
     * Constructor for SerialCommunicator.
     *
     * @param config The Arduino configuration properties
     * @param serialPortFactory The factory for creating SerialPortWrapper instances
     */
    public SerialCommunicator(ArduinoConfig config, SerialPortFactory serialPortFactory) {
        this.portName = config.getPort();
        this.baudRate = config.getBaudrate();
        this.serialPortFactory = serialPortFactory;
        initialize();
    }

    /**
     * Initializes the serial connection to the Arduino.
     */
    private void initialize() {
        try {
            log.info("Initializing serial communication on port {} with baud rate {}", portName, baudRate);

            // Find the serial port by name
            SerialPortWrapper[] ports = serialPortFactory.getCommPorts();
            for (SerialPortWrapper port : ports) {
                log.debug("Found serial port: {}", port.getSystemPortName());
                log.debug("Found serial port: {}", port.getDescriptivePortName());
                if (port.getSystemPortName().equals(portName) || port.getDescriptivePortName().contains(portName)) {
                    serialPort = port;
                    break;
                }
            }

            if (serialPort == null) {
                log.error("Serial port {} not found", portName);
                throw new IllegalStateException("Serial port not found: " + portName);
            }

            // Configure and open the port
            serialPort.setBaudRate(baudRate);
            serialPort.setComPortTimeouts(SerialPortWrapper.TIMEOUT_READ_SEMI_BLOCKING, COMMAND_TIMEOUT_MS, 0);

            if (!serialPort.openPort()) {
                log.error("Failed to open serial port {}", portName);
                throw new IllegalStateException("Failed to open serial port: " + portName);
            }

            // Create reader and writer
            reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream(), StandardCharsets.UTF_8));
            outputStream = serialPort.getOutputStream();

            // Wait for Arduino to initialize
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            log.info("Serial communication initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing serial communication", e);
            throw new IllegalStateException("Failed to initialize serial communication", e);
        }
    }

    /**
     * Sends a command to the Arduino and waits for a response.
     *
     * @param command The command to send
     * @param expectedResponse The expected response for success
     * @return true if the command was successful (Arduino responded with the expected response), false otherwise
     */
    public boolean sendCommand(String command, String expectedResponse) {
        if (serialPort == null || !serialPort.isOpen()) {
            log.error("Serial port is not open");
            return false;
        }

        try {
            log.debug("Sending command: {}", command);

            // Send the command
            byte[] bytes = (command + "\n").getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
            outputStream.flush();
            
            // Wait a short time for the Arduino to process the command
            try {
                Thread.sleep(COMMAND_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Read the response
            String response = reader.readLine();
            log.debug("Received response: {}", response);

            return expectedResponse.equalsIgnoreCase(response);
        } catch (IOException e) {
            log.error("Error sending command: {}", command, e);
            return false;
        }
    }

    /**
     * Checks if the serial port is ready for communication.
     *
     * @return true if the serial port is open, false otherwise
     */
    public boolean isOpen() {
        return serialPort != null && serialPort.isOpen();
    }

    /**
     * Closes the serial port and releases all resources.
     */
    public void close() {
        try {
            if (serialPort != null && serialPort.isOpen()) {
                log.info("Closing serial communication");

                // Close the streams and port
                if (reader != null) {
                    reader.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

                serialPort.closePort();
                log.info("Serial communication closed successfully");
            }
        } catch (IOException e) {
            log.error("Error closing serial communication", e);
        } finally {
            reader = null;
            outputStream = null;
            serialPort = null;
        }
    }
}