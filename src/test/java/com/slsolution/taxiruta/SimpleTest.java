package com.slsolution.taxiruta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple test to verify that the test framework is working correctly.
 * This test doesn't use Spring Boot context.
 * 
 * @author Senior Developer
 * @version 1.0
 */
class SimpleTest {

    @Test
    @DisplayName("Should execute a simple test successfully")
    void simpleTest() {
        // Simple assertion to verify JUnit is working
        assertTrue(true, "This test should always pass");
    }

    @Test
    @DisplayName("Should verify basic arithmetic")
    void testBasicArithmetic() {
        int result = 2 + 2;
        assertTrue(result == 4, "2 + 2 should equal 4");
    }
}