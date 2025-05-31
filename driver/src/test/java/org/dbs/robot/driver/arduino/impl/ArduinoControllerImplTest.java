package org.dbs.robot.driver.arduino.impl;

import org.dbs.robot.driver.arduino.serial.SerialCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArduinoControllerImplTest {

    private static final String OK_RESPONSE = "ok";
    private static final String READY_RESPONSE = "ready";

    @Mock
    private SerialCommunicator serialCommunicator;

    private ArduinoControllerImpl controller;

    @BeforeEach
    void setUp() {
        controller = new ArduinoControllerImpl(serialCommunicator);
    }

    @Test
    void controlLed_shouldSendCorrectCommand_whenTurningOn() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.controlLed("red", true);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("led(red, on)", OK_RESPONSE);
    }

    @Test
    void controlLed_shouldSendCorrectCommand_whenTurningOff() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.controlLed("red", false);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("led(red, off)", OK_RESPONSE);
    }

    @Test
    void controlLed_shouldHandleFailure() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(false);

        // Act
        boolean result = controller.controlLed("red", true);

        // Assert
        assertFalse(result);
        verify(serialCommunicator).sendCommand("led(red, on)", OK_RESPONSE);
    }

    @Test
    void positionServo_shouldSendCorrectCommand() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.positionServo("head", 90);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("servo(head, angle 90)", OK_RESPONSE);
    }

    @Test
    void sweep_shouldSendCorrectCommand() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.sweep("arm", 0, 180, 5);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("servo(arm, sweep 0 180 5)", OK_RESPONSE);
    }

    @Test
    void halfSweep_shouldSendCorrectCommand() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.halfSweep("claw", 10, 90, 3);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("servo(claw, half-sweep 10 90 3)", OK_RESPONSE);
    }

    @Test
    void reverseHalfSweep_shouldSendCorrectCommand() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.reverseHalfSweep("base", 45, 120, 7);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("servo(base, reverse-half-sweep 45 120 7)", OK_RESPONSE);
    }

    @Test
    void reverseSweep_shouldSendCorrectCommand() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.reverseSweep("wrist", 30, 150, 2);

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("servo(wrist, reverse-sweep 30 150 2)", OK_RESPONSE);
    }

    @Test
    void isReady_shouldReturnTrueWhenSerialCommunicatorIsOpenAndArduinoIsReady() {
        // Arrange
        when(serialCommunicator.isOpen()).thenReturn(true);
        when(serialCommunicator.sendCommand(anyString(), eq(READY_RESPONSE))).thenReturn(true);

        // Act
        boolean result = controller.isReady();

        // Assert
        assertTrue(result);
        verify(serialCommunicator).sendCommand("status(arduino, ok)", READY_RESPONSE);
    }

    @Test
    void isReady_shouldReturnFalseWhenSerialCommunicatorIsNotOpen() {
        // Arrange
        when(serialCommunicator.isOpen()).thenReturn(false);

        // Act
        boolean result = controller.isReady();

        // Assert
        assertFalse(result);
        verify(serialCommunicator, never()).sendCommand(anyString(), anyString());
    }

    @Test
    void isReady_shouldReturnFalseWhenArduinoIsNotReady() {
        // Arrange
        when(serialCommunicator.isOpen()).thenReturn(true);
        when(serialCommunicator.sendCommand(anyString(), eq(READY_RESPONSE))).thenReturn(false);

        // Act
        boolean result = controller.isReady();

        // Assert
        assertFalse(result);
        verify(serialCommunicator).sendCommand("status(arduino, ok)", READY_RESPONSE);
    }

    @Test
    void shutdown_shouldSendShutdownCommandAndCloseSerialCommunicator() {
        // Arrange
        when(serialCommunicator.sendCommand(anyString(), eq(OK_RESPONSE))).thenReturn(true);

        // Act
        controller.shutdown();

        // Assert
        verify(serialCommunicator).sendCommand("shutdown()", OK_RESPONSE);
        verify(serialCommunicator).close();
    }
}