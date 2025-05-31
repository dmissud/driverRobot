package org.dbs.robot.driver.arduino.serial;

import lombok.extern.slf4j.Slf4j;
import org.dbs.robot.driver.arduino.config.ArduinoConfig;

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
     * This method orchestrates the initialization process by:
     * 1. Finding the appropriate serial port
     * 2. Configuring and opening the port
     * 3. Creating input/output streams
     * 4. Waiting for the Arduino to initialize
     */
    private void initialize() {
        try {
            log.info("Initializing serial communication on port {} with baud rate {}", portName, baudRate);

            findSerialPort();
            configureAndOpenPort();
            createStreams();
            waitForArduinoInitialization();

            log.info("Serial communication initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing serial communication", e);
            throw new IllegalStateException("Failed to initialize serial communication", e);
        }
    }

    /**
     * Finds the serial port by name from the available ports.
     * 
     * @throws IllegalStateException if the specified port is not found
     */
    private void findSerialPort() {
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
    }

    /**
     * Configures and opens the serial port with the specified settings.
     * 
     * @throws IllegalStateException if the port cannot be opened
     */
    private void configureAndOpenPort() {
        serialPort.setBaudRate(baudRate);
        serialPort.setComPortTimeouts(SerialPortWrapper.TIMEOUT_READ_SEMI_BLOCKING, COMMAND_TIMEOUT_MS, 0);

        if (!serialPort.openPort()) {
            log.error("Failed to open serial port {}", portName);
            throw new IllegalStateException("Failed to open serial port: " + portName);
        }
    }

    /**
     * Creates input and output streams for communication with the serial port.
     */
    private void createStreams() {
        reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream(), StandardCharsets.UTF_8));
        outputStream = serialPort.getOutputStream();
    }

    /**
     * Waits for the Arduino to initialize after opening the port.
     * Arduino typically needs a short time to reset after a serial connection is established.
     */
    private void waitForArduinoInitialization() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Sends a command to the Arduino and waits for a response.
     * This method orchestrates the command sending process by:
     * 1. Validating the port is open
     * 2. Writing the command to the output stream
     * 3. Waiting for the Arduino to process the command
     * 4. Reading and validating the response
     *
     * @param command The command to send
     * @param expectedResponse The expected response for success
     * @return true if the command was successful (Arduino responded with the expected response), false otherwise
     */
    public boolean sendCommand(String command, String expectedResponse) {
        if (!isPortOpen()) {
            return false;
        }

        try {
            writeCommand(command);
            waitForProcessing();
            String response = readResponse();
            return validateResponse(response, expectedResponse);
        } catch (IOException e) {
            log.error("Error sending command: {}", command, e);
            return false;
        }
    }

    /**
     * Checks if the serial port is open and ready for communication.
     * 
     * @return true if the port is open, false otherwise
     */
    private boolean isPortOpen() {
        if (serialPort == null || !serialPort.isOpen()) {
            log.error("Serial port is not open");
            return false;
        }
        return true;
    }

    /**
     * Writes a command to the Arduino.
     * 
     * @param command The command to send
     * @throws IOException if an I/O error occurs
     */
    private void writeCommand(String command) throws IOException {
        log.debug("Sending command: {}", command);
        byte[] bytes = (command + "\n").getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * Waits for the Arduino to process the command.
     * Arduino typically needs a short time to process commands.
     */
    private void waitForProcessing() {
        try {
            Thread.sleep(COMMAND_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Reads the response from the Arduino.
     * 
     * @return The response string
     * @throws IOException if an I/O error occurs
     */
    private String readResponse() throws IOException {
        String response = reader.readLine();
        log.debug("Received response: {}", response);
        return response;
    }

    /**
     * Validates the response against the expected response.
     * 
     * @param response The actual response from the Arduino
     * @param expectedResponse The expected response
     * @return true if the response matches the expected response, false otherwise
     */
    private boolean validateResponse(String response, String expectedResponse) {
        return expectedResponse.equalsIgnoreCase(response);
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
     * This method ensures proper cleanup by:
     * 1. Checking if the port is open
     * 2. Closing input/output streams
     * 3. Closing the serial port
     * 4. Releasing all resources
     */
    public void close() {
        if (!isPortOpenForClosing()) {
            return;
        }

        log.info("Closing serial communication");

        closeStreams();
        closePort();
        releaseResources();

        log.info("Serial communication closed successfully");
    }

    /**
     * Checks if the serial port is open and available for closing.
     * 
     * @return true if the port is open, false otherwise
     */
    private boolean isPortOpenForClosing() {
        return serialPort != null && serialPort.isOpen();
    }

    /**
     * Closes the input and output streams.
     */
    private void closeStreams() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            log.error("Error closing streams", e);
        }
    }

    /**
     * Closes the serial port.
     */
    private void closePort() {
        try {
            serialPort.closePort();
        } catch (Exception e) {
            log.error("Error closing serial port", e);
        }
    }

    /**
     * Releases all resources by setting them to null.
     */
    private void releaseResources() {
        reader = null;
        outputStream = null;
        serialPort = null;
    }
}