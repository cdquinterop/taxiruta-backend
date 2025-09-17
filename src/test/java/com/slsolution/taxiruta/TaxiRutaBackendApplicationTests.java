package com.slsolution.taxiruta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Integration tests for the TaxiRuta Backend Application.
 * Tests the main application context loading and basic configuration.
 * 
 * @author Senior Developer
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TaxiRutaBackendApplicationTests {

	@Test
	@DisplayName("Should load application context successfully")
	void contextLoads() {
		// Test that the application context loads correctly
		// If this test passes, it means all beans are properly configured
		assertDoesNotThrow(() -> {
			// Context loading is verified by the test framework
		});
	}

}
