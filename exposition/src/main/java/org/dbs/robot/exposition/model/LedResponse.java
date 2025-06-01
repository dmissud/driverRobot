package org.dbs.robot.exposition.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response model for LED control endpoint.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LedResponse extends ApiResponse {
    private String name;
    private boolean state;

    /**
     * Constructor with all fields.
     *
     * @param success Whether the operation was successful
     * @param message Additional information about the operation
     * @param name    The name of the LED
     * @param state   The state of the LED (true for on, false for off)
     */
    public LedResponse(boolean success, String message, String name, boolean state) {
        super(success, message);
        this.name = name;
        this.state = state;
    }
}