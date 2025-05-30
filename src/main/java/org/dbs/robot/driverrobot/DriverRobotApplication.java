package org.dbs.robot.driverrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Driver Robot application.
 * This application provides both a web interface and a shell interface for controlling an Arduino.
 */
@SpringBootApplication
public class DriverRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverRobotApplication.class, args);
    }

}
