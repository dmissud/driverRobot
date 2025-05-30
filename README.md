# Arduino Controller Module

This module provides a Java interface for controlling an Arduino with LEDs and servomotors via serial communication.

## Features

- Control LEDs (on/off)
- Position servomotors to specific angles
- Perform complex servo movements (sweep, half-sweep, etc.)
- Manage controller lifecycle (check if ready, shutdown)

## Architecture

The module follows the Interface Segregation Principle (ISP) with a modular architecture:

- **LedController**: Interface for controlling LEDs
- **ServoPositionController**: Interface for positioning servomotors
- **ServoMovementController**: Interface for complex servo movements
- **ControllerLifecycle**: Interface for managing controller lifecycle
- **ServoController**: Composite interface combining servo positioning and movement
- **ArduinoController**: Composite interface combining all functionality

## Configuration

Configure the Arduino controller in your `application.properties` or `application.yml`:

```properties
# Arduino configuration
arduino.port=/dev/ttyUSB0  # Serial port (default: /dev/ttyUSB0 on Linux, COM1 on Windows)
arduino.baudrate=9600      # Baud rate (default: 9600)
```

## Usage

### Spring Boot Integration

The module integrates with Spring Boot. Just autowire the `ArduinoController` interface:

```java
@Service
public class MyService {
    private final ArduinoController arduinoController;

    public MyService(ArduinoController arduinoController) {
        this.arduinoController = arduinoController;
    }

    public void doSomething() {
        // Check if Arduino is ready
        if (arduinoController.isReady()) {
            // Control LEDs
            arduinoController.controlLed("red", true);  // Turn on red LED
            
            // Position servomotors
            arduinoController.positionServo("head", 90);  // Position head servo to 90 degrees
            
            // Perform complex movements
            arduinoController.sweep("arm", 0, 180, 5);  // Sweep arm servo from 0 to 180 degrees
        }
    }
}
```

### Command-Line Interface

The module provides a command-line interface using Spring Shell:

```
# Check if Arduino is ready
arduino-status

# Control LEDs
led-control --name red --state true

# Position servomotors
servo-position --name head --angle 90

# Perform complex movements
servo-sweep --name arm --start-angle 0 --end-angle 180 --speed 5
servo-half-sweep --name claw --start-angle 10 --end-angle 90 --speed 3
servo-reverse-half-sweep --name base --start-angle 45 --end-angle 120 --speed 7
servo-reverse-sweep --name wrist --start-angle 30 --end-angle 150 --speed 2

# Shut down the controller
arduino-shutdown
```

## Arduino Protocol

The module communicates with the Arduino using the following protocol:

- **LED Control**: `led(name, state)\n` where state is either `on` or `off`
- **Servo Position**: `servo(name, angle X)\n` where X is the desired angle
- **Servo Movement**: `servo(name, movement startAngle endAngle speed)\n` where movement is one of:
  - `sweep`: Complete back-and-forth sweep
  - `half-sweep`: Half sweep in clockwise direction
  - `reverse-half-sweep`: Half sweep in counter-clockwise direction
  - `reverse-sweep`: Complete back-and-forth sweep in reverse direction
- **Status Check**: `status()\n` (Arduino responds with `ready` if ready)
- **Shutdown**: `shutdown()\n`

The Arduino responds with `ok` for successful commands or `error` for failed commands.

## Dependencies

- Java 21
- Spring Boot
- Lombok
- jSerialComm (for serial communication)