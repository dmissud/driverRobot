package org.dbs.robot.driver.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link StringUtils} class.
 */
class StringUtilsTest {

    @Test
    void reverse_shouldReturnNull_whenInputIsNull() {
        // Arrange
        String input = null;
        
        // Act
        String result = StringUtils.reverse(input);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void reverse_shouldReturnEmptyString_whenInputIsEmpty() {
        // Arrange
        String input = "";
        
        // Act
        String result = StringUtils.reverse(input);
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    void reverse_shouldReverseString_whenInputIsNormal() {
        // Arrange
        String input = "hello";
        
        // Act
        String result = StringUtils.reverse(input);
        
        // Assert
        assertEquals("olleh", result);
    }
    
    @Test
    void reverse_shouldReverseString_whenInputHasSpecialCharacters() {
        // Arrange
        String input = "hello!@#$";
        
        // Act
        String result = StringUtils.reverse(input);
        
        // Assert
        assertEquals("$#@!olleh", result);
    }
}