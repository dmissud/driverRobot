package org.dbs.robot.exposition.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response model for Arduino status endpoint.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StatusResponse extends ApiResponse {
    private boolean ready;

    /**
     * Constructor with all fields.
     *
     * @param success Whether the operation was successful
     * @param message Additional information about the operation
     * @param ready   Whether the Arduino is ready
     */
    public StatusResponse(boolean success, String message, boolean ready) {
        super(success, message);
        this.ready = ready;
    }
}