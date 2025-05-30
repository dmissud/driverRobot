package org.dbs.robot.driverrobot.arduino;

/**
 * Interface for managing the lifecycle of an Arduino controller.
 * Commands:
 * - Check if ready: status()\n (returns "ready" if ready)
 * - Shutdown: shutdown()\n
 */
public interface ControllerLifecycle {
    /**
     * Checks if the controller is ready to receive commands.
     *
     * @return true if the controller is ready, false otherwise
     */
    boolean isReady();
    
    /**
     * Shuts down the controller, closing any open connections.
     * This method should be called when the controller is no longer needed.
     */
    void shutdown();
}