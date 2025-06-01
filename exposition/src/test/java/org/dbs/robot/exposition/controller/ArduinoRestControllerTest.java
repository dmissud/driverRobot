package org.dbs.robot.exposition.controller;

import org.dbs.robot.driver.arduino.ArduinoController;
import org.dbs.robot.exposition.model.ApiResponse;
import org.dbs.robot.exposition.model.LedResponse;
import org.dbs.robot.exposition.model.ServoMovementResponse;
import org.dbs.robot.exposition.model.ServoResponse;
import org.dbs.robot.exposition.model.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArduinoRestControllerTest {

    @Mock
    private ArduinoController arduinoController;

    private ArduinoRestController arduinoRestController;

    @BeforeEach
    void setUp() {
        arduinoRestController = new ArduinoRestController(arduinoController);
    }

    @Test
    void getStatus_shouldReturnOkWithReadyMessage_whenControllerIsReady() {
        // Arrange
        when(arduinoController.isReady()).thenReturn(true);

        // Act
        ResponseEntity<StatusResponse> response = arduinoRestController.getStatus();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertTrue(response.getBody().isReady());
        assertEquals("Arduino is ready", response.getBody().getMessage());
        verify(arduinoController).isReady();
    }

    @Test
    void getStatus_shouldReturnServiceUnavailableWithNotReadyMessage_whenControllerIsNotReady() {
        // Arrange
        when(arduinoController.isReady()).thenReturn(false);

        // Act
        ResponseEntity<StatusResponse> response = arduinoRestController.getStatus();

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertFalse(response.getBody().isReady());
        assertEquals("Arduino is not ready", response.getBody().getMessage());
        verify(arduinoController).isReady();
    }

    @Test
    void controlLed_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String ledName = "red";
        boolean state = true;
        when(arduinoController.controlLed(ledName, state)).thenReturn(true);

        // Act
        ResponseEntity<LedResponse> response = arduinoRestController.controlLed(ledName, state);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(ledName, response.getBody().getName());
        assertEquals(state, response.getBody().isState());
        assertEquals("LED red turned on", response.getBody().getMessage());
        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void controlLed_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String ledName = "red";
        boolean state = false;
        when(arduinoController.controlLed(ledName, state)).thenReturn(false);

        // Act
        ResponseEntity<LedResponse> response = arduinoRestController.controlLed(ledName, state);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(ledName, response.getBody().getName());
        assertEquals(state, response.getBody().isState());
        assertEquals("Failed to control LED red", response.getBody().getMessage());
        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void positionServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(true);

        // Act
        ResponseEntity<ServoResponse> response = arduinoRestController.positionServo(servoName, angle);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals(angle, response.getBody().getAngle());
        assertEquals("Servo arm positioned at 90 degrees", response.getBody().getMessage());
        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void positionServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(false);

        // Act
        ResponseEntity<ServoResponse> response = arduinoRestController.positionServo(servoName, angle);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals(angle, response.getBody().getAngle());
        assertEquals("Failed to position servo arm", response.getBody().getMessage());
        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void sweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.sweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Servo arm sweeping from 0 to 180", response.getBody().getMessage());
        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void sweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.sweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Failed to sweep servo arm", response.getBody().getMessage());
        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.halfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("half-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Servo arm half-sweeping from 0 to 90", response.getBody().getMessage());
        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.halfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("half-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Failed to half-sweep servo arm", response.getBody().getMessage());
        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.reverseHalfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("reverse-half-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Servo arm reverse-half-sweeping from 90 to 0", response.getBody().getMessage());
        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.reverseHalfSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("reverse-half-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Failed to reverse-half-sweep servo arm", response.getBody().getMessage());
        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.reverseSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("reverse-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Servo arm reverse-sweeping from 180 to 0", response.getBody().getMessage());
        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act
        ResponseEntity<ServoMovementResponse> response = arduinoRestController.reverseSweepServo(servoName, startAngle, endAngle, speed);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals(servoName, response.getBody().getName());
        assertEquals("reverse-sweep", response.getBody().getMovementType());
        assertEquals(startAngle, response.getBody().getStartAngle());
        assertEquals(endAngle, response.getBody().getEndAngle());
        assertEquals(speed, response.getBody().getSpeed());
        assertEquals("Failed to reverse-sweep servo arm", response.getBody().getMessage());
        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void shutdown_shouldCallControllerShutdownAndReturnOkWithSuccessMessage() {
        // Act
        ResponseEntity<ApiResponse> response = arduinoRestController.shutdown();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Arduino controller shut down", response.getBody().getMessage());
        verify(arduinoController).shutdown();
    }
}