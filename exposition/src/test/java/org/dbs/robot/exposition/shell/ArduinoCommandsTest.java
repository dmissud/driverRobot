package org.dbs.robot.exposition.shell;

import org.dbs.robot.driver.arduino.ArduinoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArduinoCommandsTest {

    @Mock
    private ArduinoController arduinoController;

    private ArduinoCommands arduinoCommands;

    @BeforeEach
    void setUp() {
        arduinoCommands = new ArduinoCommands(arduinoController);
    }

    @Test
    void getStatus_shouldReturnArduinoIsReady_whenControllerIsReady() {
        // Arrange
        when(arduinoController.isReady()).thenReturn(true);

        // Act
        String result = arduinoCommands.getStatus();

        // Assert
        assertEquals("Arduino is ready", result);
        verify(arduinoController).isReady();
    }

    @Test
    void getStatus_shouldReturnArduinoIsNotReady_whenControllerIsNotReady() {
        // Arrange
        when(arduinoController.isReady()).thenReturn(false);

        // Act
        String result = arduinoCommands.getStatus();

        // Assert
        assertEquals("Arduino is not ready", result);
        verify(arduinoController).isReady();
    }

    @Test
    void controlLed_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String ledName = "red";
        boolean state = true;
        when(arduinoController.controlLed(ledName, state)).thenReturn(true);

        // Act
        String result = arduinoCommands.controlLed(ledName, state);

        // Assert
        assertEquals("LED red turned on", result);
        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void controlLed_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String ledName = "red";
        boolean state = false;
        when(arduinoController.controlLed(ledName, state)).thenReturn(false);

        // Act
        String result = arduinoCommands.controlLed(ledName, state);

        // Assert
        assertEquals("Failed to control LED red", result);
        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void positionServo_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(true);

        // Act
        String result = arduinoCommands.positionServo(servoName, angle);

        // Assert
        assertEquals("Servo arm positioned at 90 degrees", result);
        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void positionServo_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(false);

        // Act
        String result = arduinoCommands.positionServo(servoName, angle);

        // Assert
        assertEquals("Failed to position servo arm", result);
        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void sweepServo_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        String result = arduinoCommands.sweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Servo arm sweeping from 0 to 180", result);
        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void sweepServo_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        String result = arduinoCommands.sweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Failed to sweep servo arm", result);
        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        String result = arduinoCommands.halfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Servo arm half-sweeping from 0 to 90", result);
        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        String result = arduinoCommands.halfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Failed to half-sweep servo arm", result);
        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        String result = arduinoCommands.reverseHalfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Servo arm reverse-half-sweeping from 90 to 0", result);
        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        String result = arduinoCommands.reverseHalfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Failed to reverse-half-sweep servo arm", result);
        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        String result = arduinoCommands.reverseSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Servo arm reverse-sweeping from 180 to 0", result);
        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        String result = arduinoCommands.reverseSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals("Failed to reverse-sweep servo arm", result);
        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void shutdown_shouldCallControllerShutdownAndReturnSuccessMessage() {
        // Act
        String result = arduinoCommands.shutdown();

        // Assert
        assertEquals("Arduino controller shut down", result);
        verify(arduinoController).shutdown();
    }
}