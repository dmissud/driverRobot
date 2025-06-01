package org.dbs.robot.exposition.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response model for servo position endpoint.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServoResponse extends ApiResponse {
    private String name;
    private int angle;

    /**
     * Constructor with all fields.
     *
     * @param success Whether the operation was successful
     * @param message Additional information about the operation
     * @param name    The name of the servo
     * @param angle   The angle of the servo
     */
    public ServoResponse(boolean success, String message, String name, int angle) {
        super(success, message);
        this.name = name;
        this.angle = angle;
    }
}