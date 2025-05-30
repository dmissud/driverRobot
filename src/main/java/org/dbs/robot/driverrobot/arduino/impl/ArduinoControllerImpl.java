package org.dbs.robot.driverrobot.arduino.impl;

import lombok.extern.slf4j.Slf4j;
import org.dbs.robot.driverrobot.arduino.ArduinoController;
import org.dbs.robot.driverrobot.arduino.serial.SerialCommunicator;

/**
 * Implementation of the ArduinoController interface for controlling an Arduino via serial communication.
 * This class is responsible for converting high-level commands to the Arduino protocol format
 * and delegating the actual communication to the SerialCommunicator.
 */
@Slf4j
public class ArduinoControllerImpl implements ArduinoController {

    private static final String OK_RESPONSE = "ok";
    private static final String READY_RESPONSE = "ready";

    private final SerialCommunicator serialCommunicator;

    /**
     * Constructor for ArduinoControllerImpl.
     *
     * @param serialCommunicator The serial communicator for Arduino communication
     */
    public ArduinoControllerImpl(SerialCommunicator serialCommunicator) {
        this.serialCommunicator = serialCommunicator;
        log.info("Arduino controller initialized with serial communicator");
    }

    @Override
    public boolean controlLed(String name, boolean state) {
        String stateStr = state ? "on" : "off";
        String command = String.format("led(%s, %s)", name, stateStr);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean positionServo(String name, int angle) {
        String command = String.format("servo(%s, angle %d)", name, angle);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean sweep(String name, int startAngle, int endAngle, int speed) {
        String command = String.format("servo(%s, sweep %d %d %d)", name, startAngle, endAngle, speed);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean halfSweep(String name, int startAngle, int endAngle, int speed) {
        String command = String.format("servo(%s, half-sweep %d %d %d)", name, startAngle, endAngle, speed);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean reverseHalfSweep(String name, int startAngle, int endAngle, int speed) {
        String command = String.format("servo(%s, reverse-half-sweep %d %d %d)", name, startAngle, endAngle, speed);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean reverseSweep(String name, int startAngle, int endAngle, int speed) {
        String command = String.format("servo(%s, reverse-sweep %d %d %d)", name, startAngle, endAngle, speed);
        return serialCommunicator.sendCommand(command, OK_RESPONSE);
    }

    @Override
    public boolean isReady() {
        if (!serialCommunicator.isOpen()) {
            return false;
        }
        return serialCommunicator.sendCommand("status(arduino, ok)", READY_RESPONSE);
    }

    @Override
    public void shutdown() {
        log.info("Shutting down Arduino controller");
        serialCommunicator.sendCommand("shutdown()", OK_RESPONSE);
        serialCommunicator.close();
        log.info("Arduino controller shut down successfully");
    }
}