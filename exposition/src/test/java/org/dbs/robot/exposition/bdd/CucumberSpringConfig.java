package org.dbs.robot.exposition.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.dbs.robot.DriverRobotApplication;
import org.dbs.robot.driver.arduino.ArduinoController;
import org.dbs.robot.driver.arduino.serial.SerialCommunicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@CucumberContextConfiguration
@SpringBootTest(classes = DriverRobotApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfig {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected ArduinoController arduinoController;

    // Prevent real serial initialization during tests
    @MockitoBean
    protected SerialCommunicator serialCommunicator;
}
