---
mode: agent
model: claude Sonnet 4
---
Provide instructions to AI to automatically generate tests whenever a new REST endpoint is created in the TaxiRuta Backend project.

Rules:
1. **Automatic Test Generation**:
   - Every time a new endpoint is created, AI must generate the corresponding **unit tests** and **integration tests**.
   - Unit tests should cover the **service layer** logic and edge cases.
   - Integration tests should cover the **controller endpoints**, including request validation, authentication, and response correctness.

2. **Test Conventions**:
   - Use **JUnit 5**, **Mockito** for mocking dependencies, and **Spring Boot Test** for integration.
   - Name test classes following the convention: `{ClassName}Test` (e.g., `TripControllerTest`, `BookingServiceTest`).
   - Test method names should clearly describe the scenario (e.g., `shouldReturnTripWhenIdExists`, `shouldFailWhenUserNotFound`).

3. **Coverage**:
   - Positive scenarios (successful requests) and negative scenarios (validation errors, unauthorized access, resource not found) must be covered.
   - Ensure **full coverage** for the new endpoint’s service and controller logic.

4. **Consistency**:
   - Use the same **DTOs** as defined for the endpoint.
   - Keep all **comments in English** in the tests.
   - Organize test classes in the corresponding package structure:
     ```
     src/test/java/com/yourcompany/taxiroute/controller/
     src/test/java/com/yourcompany/taxiroute/service/
     src/test/java/com/yourcompany/taxiroute/repository/
     ```

5. **Outcome**:
   - Each endpoint will automatically have corresponding tests ready for execution.
   - Tests will validate request/response contracts, authentication, validation, and business logic.
