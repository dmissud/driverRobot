package org.dbs.robot.driverrobot.shell;

import lombok.RequiredArgsConstructor;
import org.dbs.robot.driverrobot.arduino.ArduinoController;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Spring Shell commands for Arduino operations.
 * This class provides command-line commands to control LEDs and servomotors.
 */
@ShellComponent
@RequiredArgsConstructor
public class ArduinoCommands {

    private final ArduinoController arduinoController;

    /**
     * Checks if the Arduino is ready.
     *
     * @return Status message
     */
    @ShellMethod(value = "Check if Arduino is ready", key = "arduino-status")
    public String getStatus() {
        if (arduinoController.isReady()) {
            return "Arduino is ready";
        } else {
            return "Arduino is not ready";
        }
    }

    /**
     * Controls an LED.
     *
     * @param name  The name of the LED
     * @param state The state to set (true for on, false for off)
     * @return Status message
     */
    @ShellMethod(value = "Control an LED", key = "led-control")
    public String controlLed(
            @ShellOption(help = "LED name") String name,
            @ShellOption(help = "LED state (true for on, false for off)") boolean state) {
        if (arduinoController.controlLed(name, state)) {
            return "LED " + name + " " + (state ? "turned on" : "turned off");
        } else {
            return "Failed to control LED " + name;
        }
    }

    /**
     * Positions a servomotor.
     *
     * @param name  The name of the servomotor
     * @param angle The angle to position the servomotor
     * @return Status message
     */
    @ShellMethod(value = "Position a servomotor", key = "servo-position")
    public String positionServo(
            @ShellOption(help = "Servo name") String name,
            @ShellOption(help = "Angle (0-180)") int angle) {
        if (arduinoController.positionServo(name, angle)) {
            return "Servo " + name + " positioned at " + angle + " degrees";
        } else {
            return "Failed to position servo " + name;
        }
    }

    /**
     * Performs a sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return Status message
     */
    @ShellMethod(value = "Perform a sweep movement on a servomotor", key = "servo-sweep")
    public String sweepServo(
            @ShellOption(help = "Servo name") String name,
            @ShellOption(help = "Start angle") int startAngle,
            @ShellOption(help = "End angle") int endAngle,
            @ShellOption(help = "Speed (1-10)") int speed) {
        if (arduinoController.sweep(name, startAngle, endAngle, speed)) {
            return "Servo " + name + " sweeping from " + startAngle + " to " + endAngle;
        } else {
            return "Failed to sweep servo " + name;
        }
    }

    /**
     * Performs a half-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return Status message
     */
    @ShellMethod(value = "Perform a half-sweep movement on a servomotor", key = "servo-half-sweep")
    public String halfSweepServo(
            @ShellOption(help = "Servo name") String name,
            @ShellOption(help = "Start angle") int startAngle,
            @ShellOption(help = "End angle") int endAngle,
            @ShellOption(help = "Speed (1-10)") int speed) {
        if (arduinoController.halfSweep(name, startAngle, endAngle, speed)) {
            return "Servo " + name + " half-sweeping from " + startAngle + " to " + endAngle;
        } else {
            return "Failed to half-sweep servo " + name;
        }
    }

    /**
     * Performs a reverse-half-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return Status message
     */
    @ShellMethod(value = "Perform a reverse-half-sweep movement on a servomotor", key = "servo-reverse-half-sweep")
    public String reverseHalfSweepServo(
            @ShellOption(help = "Servo name") String name,
            @ShellOption(help = "Start angle") int startAngle,
            @ShellOption(help = "End angle") int endAngle,
            @ShellOption(help = "Speed (1-10)") int speed) {
        if (arduinoController.reverseHalfSweep(name, startAngle, endAngle, speed)) {
            return "Servo " + name + " reverse-half-sweeping from " + startAngle + " to " + endAngle;
        } else {
            return "Failed to reverse-half-sweep servo " + name;
        }
    }

    /**
     * Performs a reverse-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return Status message
     */
    @ShellMethod(value = "Perform a reverse-sweep movement on a servomotor", key = "servo-reverse-sweep")
    public String reverseSweepServo(
            @ShellOption(help = "Servo name") String name,
            @ShellOption(help = "Start angle") int startAngle,
            @ShellOption(help = "End angle") int endAngle,
            @ShellOption(help = "Speed (1-10)") int speed) {
        if (arduinoController.reverseSweep(name, startAngle, endAngle, speed)) {
            return "Servo " + name + " reverse-sweeping from " + startAngle + " to " + endAngle;
        } else {
            return "Failed to reverse-sweep servo " + name;
        }
    }

    /**
     * Shuts down the Arduino controller.
     *
     * @return Status message
     */
    @ShellMethod(value = "Shut down the Arduino controller", key = "arduino-shutdown")
    public String shutdown() {
        arduinoController.shutdown();
        return "Arduino controller shut down";
    }
}