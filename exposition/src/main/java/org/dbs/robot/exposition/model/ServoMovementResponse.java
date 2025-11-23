package org.dbs.robot.exposition.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response model for servo movement endpoints (sweep, half-sweep, etc.).
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServoMovementResponse extends ApiResponse {
    private String name;
    private String movementType;
    private int startAngle;
    private int endAngle;
    private int speed;

    /**
     * Constructor with all fields.
     *
     * @param success      Whether the operation was successful
     * @param message      Additional information about the operation
     * @param name         The name of the servo
     * @param movementType The type of movement (sweep, half-sweep, etc.)
     * @param startAngle   The starting angle of the movement
     * @param endAngle     The ending angle of the movement
     * @param speed        The speed of the movement
     */
    public ServoMovementResponse(boolean success, String message, String name, String movementType,
                                int startAngle, int endAngle, int speed) {
        super(success, message);
        this.name = name;
        this.movementType = movementType;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.speed = speed;
    }
}