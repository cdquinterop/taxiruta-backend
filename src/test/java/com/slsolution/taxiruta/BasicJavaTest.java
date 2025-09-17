package com.slsolution.taxiruta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit test to validate test framework functionality.
 * No Spring Boot dependencies - pure JUnit test.
 * 
 * @author Senior Developer
 * @version 1.0
 */
class BasicJavaTest {

    @Test
    @DisplayName("Basic test to validate JUnit framework")
    void shouldExecuteBasicTest() {
        // Given
        String expected = "TaxiRuta Backend";
        String actual = "TaxiRuta Backend";
        
        // When & Then
        assertEquals(expected, actual, "Strings should be equal");
        assertTrue(true, "This should always pass");
        assertNotNull(expected, "Expected should not be null");
    }

    @Test
    @DisplayName("Test basic math operations")
    void shouldPerformBasicMathOperations() {
        // Given
        int a = 5;
        int b = 3;
        
        // When
        int sum = a + b;
        int product = a * b;
        
        // Then
        assertEquals(8, sum, "Sum should be 8");
        assertEquals(15, product, "Product should be 15");
    }
}