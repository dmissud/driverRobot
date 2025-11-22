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
import org.dbs.robot.exposition.model.LedResponse;
import org.dbs.robot.exposition.model.ServoMovementResponse;
import org.dbs.robot.exposition.model.ServoResponse;
import org.dbs.robot.exposition.model.StatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Arduino is not ready",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            )
    })
    public ResponseEntity<StatusResponse> getStatus() {
        boolean isReady = arduinoController.isReady();
        StatusResponse response;

        if (isReady) {
            response = new StatusResponse(true, "Arduino is ready", true);
            return ResponseEntity.ok(response);
        } else {
            response = new StatusResponse(false, "Arduino is not ready", false);
            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * Text/plain variant for status endpoint to satisfy HTTP tests expecting plain text.
     */
    @GetMapping(value = "/status", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getStatusText() {
        boolean isReady = arduinoController.isReady();
        if (isReady) {
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to control LED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LedResponse.class))
            )
    })
    @PostMapping(value = "/led/{name}")
    public ResponseEntity<LedResponse> controlLed(
            @Parameter(description = "LED name identifier", required = true) @PathVariable String name,
            @Parameter(description = "LED state (true for on, false for off)", required = true) @RequestParam boolean state) {
        if (arduinoController.controlLed(name, state)) {
            LedResponse response = new LedResponse(true, "LED " + name + " " + (state ? "turned on" : "turned off"), name, state);
            return ResponseEntity.ok(response);
        } else {
            // Correction XSS : On échappe le nom avant de le mettre dans la réponse
            String safeName = HtmlUtils.htmlEscape(name);
            LedResponse response = new LedResponse(false, "Failed to control LED " + safeName, safeName, state);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for LED control to satisfy HTTP tests expecting simple message.
     */
    @PostMapping(value = "/led/{name}", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> controlLedText(@PathVariable String name, @RequestParam boolean state) {
        if (arduinoController.controlLed(name, state)) {
            return ResponseEntity.ok("LED " + name + " " + (state ? "turned on" : "turned off"));
        } else {
            // Correction XSS
            return ResponseEntity.status(500).body("Failed to control LED " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to position servo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoResponse.class))
            )
    })
    @PostMapping(value = "/servo/{name}/position")
    public ResponseEntity<ServoResponse> positionServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Angle in degrees (typically 0-180)", required = true) @RequestParam int angle) {
        if (arduinoController.positionServo(name, angle)) {
            ServoResponse response = new ServoResponse(true, SERVO + name + " positioned at " + angle + " degrees", name, angle);
            return ResponseEntity.ok(response);
        } else {
            // Correction XSS
            String safeName = HtmlUtils.htmlEscape(name);
            ServoResponse response = new ServoResponse(false, "Failed to position servo " + safeName, safeName, angle);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for servo position.
     */
    @PostMapping(value = "/servo/{name}/position", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> positionServoText(@PathVariable String name, @RequestParam int angle) {
        if (arduinoController.positionServo(name, angle)) {
            return ResponseEntity.ok(SERVO + name + " positioned at " + angle + " degrees");
        } else {
            // Correction XSS
            return ResponseEntity.status(500).body("Failed to position servo " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            ),
            @ApiResponse(
                responseCode = "500", 
                description = "Failed to sweep servo",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            )
        })
        @PostMapping(value = "/servo/{name}/sweep")
        public ResponseEntity<ServoMovementResponse> sweepServo(
                @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
                @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
                @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
                @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
            if (arduinoController.sweep(name, startAngle, endAngle, speed)) {
            ServoMovementResponse response = new ServoMovementResponse(
                    true,
                    SERVO + name + " sweeping from " + startAngle + " to " + endAngle,
                    name,
                    "sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.ok(response);
        } else {
            ServoMovementResponse response = new ServoMovementResponse(
                    false,
                    "Failed to sweep servo " + HtmlUtils.htmlEscape(name),
                    HtmlUtils.htmlEscape(name),
                    "sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for servo sweep.
     */
    @PostMapping(value = "/servo/{name}/sweep", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> sweepServoText(@PathVariable String name,
                                                 @RequestParam int startAngle,
                                                 @RequestParam int endAngle,
                                                 @RequestParam int speed) {
        if (arduinoController.sweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to sweep servo " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            ),
            @ApiResponse(
                responseCode = "500", 
                description = "Failed to half-sweep servo",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            )
        })
        @PostMapping(value = "/servo/{name}/half-sweep")
        public ResponseEntity<ServoMovementResponse> halfSweepServo(
                @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
                @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
                @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
                @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
            if (arduinoController.halfSweep(name, startAngle, endAngle, speed)) {
            ServoMovementResponse response = new ServoMovementResponse(
                    true,
                    SERVO + name + " half-sweeping from " + startAngle + " to " + endAngle,
                    name,
                    "half-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.ok(response);
        } else {
            ServoMovementResponse response = new ServoMovementResponse(
                    false,
                    "Failed to half-sweep servo " + HtmlUtils.htmlEscape(name),
                    HtmlUtils.htmlEscape(name),
                    "half-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for servo half-sweep.
     */
    @PostMapping(value = "/servo/{name}/half-sweep", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> halfSweepServoText(@PathVariable String name,
                                                     @RequestParam int startAngle,
                                                     @RequestParam int endAngle,
                                                     @RequestParam int speed) {
        if (arduinoController.halfSweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " half-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to half-sweep servo " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            ),
            @ApiResponse(
                responseCode = "500", 
                description = "Failed to reverse-half-sweep servo",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            )
        })
        @PostMapping(value = "/servo/{name}/reverse-half-sweep")
        public ResponseEntity<ServoMovementResponse> reverseHalfSweepServo(
                @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
                @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
                @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
                @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
            if (arduinoController.reverseHalfSweep(name, startAngle, endAngle, speed)) {
            ServoMovementResponse response = new ServoMovementResponse(
                    true,
                    SERVO + name + " reverse-half-sweeping from " + startAngle + " to " + endAngle,
                    name,
                    "reverse-half-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.ok(response);
        } else {
            ServoMovementResponse response = new ServoMovementResponse(
                    false,
                    "Failed to reverse-half-sweep servo " + HtmlUtils.htmlEscape(name),
                    HtmlUtils.htmlEscape(name),
                    "reverse-half-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for servo reverse-half-sweep.
     */
    @PostMapping(value = "/servo/{name}/reverse-half-sweep", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> reverseHalfSweepServoText(@PathVariable String name,
                                                            @RequestParam int startAngle,
                                                            @RequestParam int endAngle,
                                                            @RequestParam int speed) {
        if (arduinoController.reverseHalfSweep(HtmlUtils.htmlEscape(name), startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + HtmlUtils.htmlEscape(name) + " reverse-half-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to reverse-half-sweep servo " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to reverse-sweep servo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServoMovementResponse.class))
            )
    })
    @PostMapping(value = "/servo/{name}/reverse-sweep", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ResponseEntity<ServoMovementResponse> reverseSweepServo(
            @Parameter(description = "Servo name identifier", required = true) @PathVariable String name,
            @Parameter(description = "Starting angle in degrees", required = true) @RequestParam int startAngle,
            @Parameter(description = "Ending angle in degrees", required = true) @RequestParam int endAngle,
            @Parameter(description = "Speed of movement (higher values mean faster movement)", required = true) @RequestParam int speed) {
        if (arduinoController.reverseSweep(name, startAngle, endAngle, speed)) {
            ServoMovementResponse response = new ServoMovementResponse(
                    true,
                    SERVO + name + " reverse-sweeping from " + startAngle + " to " + endAngle,
                    name,
                    "reverse-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.ok(response);
        } else {
            ServoMovementResponse response = new ServoMovementResponse(
                    false,
                    "Failed to reverse-sweep servo " + HtmlUtils.htmlEscape(name),
                    HtmlUtils.htmlEscape(name),
                    "reverse-sweep",
                    startAngle,
                    endAngle,
                    speed
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Text/plain variant for servo reverse-sweep.
     */
    @PostMapping(value = "/servo/{name}/reverse-sweep", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE, consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> reverseSweepServoText(@PathVariable String name,
                                                        @RequestParam int startAngle,
                                                        @RequestParam int endAngle,
                                                        @RequestParam int speed) {
        if (arduinoController.reverseSweep(name, startAngle, endAngle, speed)) {
            return ResponseEntity.ok(SERVO + name + " reverse-sweeping from " + startAngle + " to " + endAngle);
        } else {
            return ResponseEntity.status(500).body("Failed to reverse-sweep servo " + HtmlUtils.htmlEscape(name));
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = org.dbs.robot.exposition.model.ApiResponse.class))
            )
    })
    public ResponseEntity<org.dbs.robot.exposition.model.ApiResponse> shutdown() {
        arduinoController.shutdown();
        org.dbs.robot.exposition.model.ApiResponse response = new org.dbs.robot.exposition.model.ApiResponse(true, "Arduino controller shut down");
        return ResponseEntity.ok(response);
    }

    /**
     * Text/plain variant for shutdown.
     */
    @PostMapping(value = "/shutdown", produces = org.springframework.http.MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> shutdownText() {
        arduinoController.shutdown();
        return ResponseEntity.ok("Arduino controller shut down");
    }
}
