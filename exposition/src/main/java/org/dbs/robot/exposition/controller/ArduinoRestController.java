package org.dbs.robot.exposition.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dbs.robot.driver.arduino.ArduinoController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Arduino operations.
 * This controller provides HTTP endpoints to control LEDs and servomotors.
 */
@RestController
@RequestMapping("/api/arduino")
@RequiredArgsConstructor
@Tag(name = "Arduino Controller", description = "API for controlling Arduino devices with LEDs and servomotors")
public class ArduinoRestController {

    public static final String SERVO = "Servo ";
    private final ArduinoController arduinoController;

    /**
     * Checks if the Arduino is ready.
     *
     * @return HTTP 200 OK if ready, HTTP 503 Service Unavailable if not ready
     */
    @Operation(
        summary = "Check Arduino status",
        description = "Checks if the Arduino controller is ready to receive commands"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Arduino is ready",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "503", 
            description = "Arduino is not ready",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        if (arduinoController.isReady()) {
            return ResponseEntity.ok("Arduino is ready");
        } else {
            return ResponseEntity.status(503).body("Arduino is not ready");
        }
    }

    /**
     * Controls an LED.
     *
     * @param name  The name of the LED
     * @param state The state to set (true for on, false for off)
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Control LED",
        description = "Turns an LED on or off"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "LED state changed successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to control LED",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/led/{name}")
    public ResponseEntity<String> controlLed(
            @Parameter(description = "LED name identifier", required = true) @PathVariable String name,
            @Parameter(description = "LED state (true for on, false for off)", required = true) @RequestParam boolean state) {
        if (arduinoController.controlLed(name, state)) {
            return ResponseEntity.ok("LED " + name + " " + (state ? "turned on" : "turned off"));
        } else {
            return ResponseEntity.status(500).body("Failed to control LED " + name);
        }
    }

    /**
     * Positions a servomotor.
     *
     * @param name  The name of the servomotor
     * @param angle The angle to position the servomotor
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Position servomotor",
        description = "Positions a servomotor to a specific angle"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Servo positioned successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to position servo",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/servo/{name}/position")
    public ResponseEntity<String> positionServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Angle in degrees (typically 0-180)", required = true) @RequestParam int angle) {
        if (arduinoController.positionServo(name, angle)) {
            return ResponseEntity.ok(SERVO + name + " positioned at " + angle + " degrees");
        } else {
            return ResponseEntity.status(500).body("Failed to position servo " + name);
        }
    }

    /**
     * Performs a sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Perform sweep movement",
        description = "Performs a complete back-and-forth sweep movement on a servomotor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Servo sweep initiated successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to sweep servo",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/servo/{name}/sweep")
    public ResponseEntity<String> sweepServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
            @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
            @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
        if (arduinoController.sweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to sweep servo " + name);
        }
    }

    /**
     * Performs a half-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Perform half-sweep movement",
        description = "Performs a half-sweep movement on a servomotor in clockwise direction"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Servo half-sweep initiated successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to half-sweep servo",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/servo/{name}/half-sweep")
    public ResponseEntity<String> halfSweepServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
            @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
            @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
        if (arduinoController.halfSweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " half-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to half-sweep servo " + name);
        }
    }

    /**
     * Performs a reverse-half-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Perform reverse-half-sweep movement",
        description = "Performs a half-sweep movement on a servomotor in counter-clockwise direction"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Servo reverse-half-sweep initiated successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to reverse-half-sweep servo",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/servo/{name}/reverse-half-sweep")
    public ResponseEntity<String> reverseHalfSweepServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
            @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
            @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
        if (arduinoController.reverseHalfSweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " reverse-half-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to reverse-half-sweep servo " + name);
        }
    }

    /**
     * Performs a reverse-sweep movement on a servomotor.
     *
     * @param name       The name of the servomotor
     * @param startAngle The starting angle of the sweep
     * @param endAngle   The ending angle of the sweep
     * @param speed      The speed of the sweep
     * @return HTTP 200 OK if successful, HTTP 500 Internal Server Error if failed
     */
    @Operation(
        summary = "Perform reverse-sweep movement",
        description = "Performs a complete back-and-forth sweep movement on a servomotor in reverse direction"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Servo reverse-sweep initiated successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to reverse-sweep servo",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/servo/{name}/reverse-sweep")
    public ResponseEntity<String> reverseSweepServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
            @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
            @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
        if (arduinoController.reverseSweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " reverse-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to reverse-sweep servo " + name);
        }
    }

    /**
     * Shuts down the Arduino controller.
     *
     * @return HTTP 200 OK
     */
    @Operation(
        summary = "Shutdown Arduino controller",
        description = "Shuts down the Arduino controller, closing any open connections"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Arduino controller shut down successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        )
    })
    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdown() {
        arduinoController.shutdown();
        return ResponseEntity.ok("Arduino controller shut down");
    }
}
