package org.dbs.robot.driver.arduino.config;

import org.dbs.robot.driver.arduino.ArduinoController;
import org.dbs.robot.driver.arduino.impl.ArduinoControllerImpl;
import org.dbs.robot.driver.arduino.serial.JSerialCommFactory;
import org.dbs.robot.driver.arduino.serial.SerialCommunicator;
import org.dbs.robot.driver.arduino.serial.SerialPortFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for Arduino controller.
 * This class enables the ArduinoConfig properties and creates the ArduinoController bean.
 */
@Configuration
@EnableConfigurationProperties(ArduinoConfig.class)
public class ArduinoConfiguration {

    /**
     * Creates a SerialPortFactory bean.
     *
     * @return An instance of SerialPortFactory
     */
    @Bean
    public SerialPortFactory serialPortFactory() {
        return new JSerialCommFactory();
    }

    /**
     * Creates a SerialCommunicator bean for Arduino communication.
     *
     * @param config The Arduino configuration properties
     * @param serialPortFactory The factory for creating SerialPortWrapper instances
     * @return An instance of SerialCommunicator
     */
    @Bean
    public SerialCommunicator serialCommunicator(ArduinoConfig config, SerialPortFactory serialPortFactory) {
        return new SerialCommunicator(config, serialPortFactory);
    }

    /**
     * Creates an ArduinoController bean using the SerialCommunicator.
     *
     * @param serialCommunicator The serial communicator for Arduino communication
     * @return An instance of ArduinoController
     */
    @Bean
    public ArduinoController arduinoController(SerialCommunicator serialCommunicator) {
        return new ArduinoControllerImpl(serialCommunicator);
    }
}