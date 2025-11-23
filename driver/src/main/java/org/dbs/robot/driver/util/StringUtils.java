package org.dbs.robot.driver.util;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    private StringUtils() {}
    /**
     * Reverses a string.
     *
     * @param input The string to reverse
     * @return The reversed string
     */
    public static String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }
}