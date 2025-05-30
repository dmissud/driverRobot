package org.dbs.robot.driverrobot.arduino.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Arduino controller.
 * These properties can be set in application.properties or application.yml.
 * 
 * Example:
 * arduino.port=/dev/ttyUSB0
 * arduino.baudrate=9600
 */
@Data
@ConfigurationProperties(prefix = "arduino")
public class ArduinoConfig {

    /**
     * The name of the serial port to connect to.
     * Default is /dev/ttyUSB0 on Linux or COM1 on Windows.
     */
    private String port = "/dev/ttyUSB0";

    /**
     * The baud rate for the serial connection.
     * Default is 9600.
     */
    private int baudrate = 9600;
}
