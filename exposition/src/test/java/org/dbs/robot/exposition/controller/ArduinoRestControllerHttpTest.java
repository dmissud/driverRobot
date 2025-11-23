package org.dbs.robot.exposition.controller;

import org.dbs.robot.DriverRobotApplication;
import org.dbs.robot.driver.arduino.ArduinoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DriverRobotApplication.class)
@AutoConfigureMockMvc
class ArduinoRestControllerHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArduinoController arduinoController;

    @BeforeEach
    void setUp() {
        // Reset the mock before each test
        reset(arduinoController);
    }

    @Test
    void getStatus_shouldReturnOkWithReadyMessage_whenControllerIsReady() throws Exception {
        // Arrange
        when(arduinoController.isReady()).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/arduino/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Arduino is ready"));

        verify(arduinoController).isReady();
    }

    @Test
    void getStatus_shouldReturnServiceUnavailableWithNotReadyMessage_whenControllerIsNotReady() throws Exception {
        // Arrange
        when(arduinoController.isReady()).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/arduino/status"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Arduino is not ready"));

        verify(arduinoController).isReady();
    }

    @Test
    void controlLed_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String ledName = "red";
        boolean state = true;
        when(arduinoController.controlLed(ledName, state)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/led/{name}", ledName)
                        .param("state", String.valueOf(state))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("LED red turned on"));

        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void controlLed_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String ledName = "red";
        boolean state = false;
        when(arduinoController.controlLed(ledName, state)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/led/{name}", ledName)
                        .param("state", String.valueOf(state))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to control LED red"));

        verify(arduinoController).controlLed(ledName, state);
    }

    @Test
    void positionServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/position", servoName)
                        .param("angle", String.valueOf(angle))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Servo arm positioned at 90 degrees"));

        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void positionServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String servoName = "arm";
        int angle = 90;
        when(arduinoController.positionServo(servoName, angle)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/position", servoName)
                        .param("angle", String.valueOf(angle))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to position servo arm"));

        verify(arduinoController).positionServo(servoName, angle);
    }

    @Test
    void sweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Servo arm sweeping from 0 to 180"));

        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void sweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 180;
        int speed = 5;
        when(arduinoController.sweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to sweep servo arm"));

        verify(arduinoController).sweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/half-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Servo arm half-sweeping from 0 to 90"));

        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void halfSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 0;
        int endAngle = 90;
        int speed = 5;
        when(arduinoController.halfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/half-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to half-sweep servo arm"));

        verify(arduinoController).halfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/reverse-half-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Servo arm reverse-half-sweeping from 90 to 0"));

        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseHalfSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 90;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseHalfSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/reverse-half-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to reverse-half-sweep servo arm"));

        verify(arduinoController).reverseHalfSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnOkWithSuccessMessage_whenControllerReturnsTrue() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/reverse-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Servo arm reverse-sweeping from 180 to 0"));

        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void reverseSweepServo_shouldReturnInternalServerErrorWithFailureMessage_whenControllerReturnsFalse() throws Exception {
        // Arrange
        String servoName = "arm";
        int startAngle = 180;
        int endAngle = 0;
        int speed = 5;
        when(arduinoController.reverseSweep(servoName, startAngle, endAngle, speed)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/arduino/servo/{name}/reverse-sweep", servoName)
                        .param("startAngle", String.valueOf(startAngle))
                        .param("endAngle", String.valueOf(endAngle))
                        .param("speed", String.valueOf(speed))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to reverse-sweep servo arm"));

        verify(arduinoController).reverseSweep(servoName, startAngle, endAngle, speed);
    }

    @Test
    void shutdown_shouldCallControllerShutdownAndReturnOkWithSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/arduino/shutdown"))
                .andExpect(status().isOk())
                .andExpect(content().string("Arduino controller shut down"));

        verify(arduinoController).shutdown();
    }
}
